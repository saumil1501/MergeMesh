package com.mergemesh.backend.invitation;

import java.util.List;
import java.util.Locale;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.mergemesh.backend.user.User;
import com.mergemesh.backend.user.UserRepository;
import com.mergemesh.backend.workspace.Membership;
import com.mergemesh.backend.workspace.MembershipRepository;
import com.mergemesh.backend.workspace.Workspace;
import com.mergemesh.backend.workspace.WorkspaceRepository;

@Service
public class InvitationService {

    private final InvitationRepository invitationRepository;
    private final WorkspaceRepository workspaceRepository;
    private final MembershipRepository membershipRepository;
    private final UserRepository userRepository;

    public InvitationService(
            InvitationRepository invitationRepository,
            WorkspaceRepository workspaceRepository,
            MembershipRepository membershipRepository,
            UserRepository userRepository) {

        this.invitationRepository = invitationRepository;
        this.workspaceRepository = workspaceRepository;
        this.membershipRepository = membershipRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public InvitationResponse invite(
            UUID workspaceId,
            UUID inviterUserId,
            CreateInvitationRequest request) {

        Workspace workspace = workspaceRepository.findById(workspaceId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Workspace not found"));

        if (!workspace.getOwnerUserId().equals(inviterUserId)) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Only the workspace owner can invite members");
        }

        String email = request.email().trim().toLowerCase(Locale.ROOT);

        User invitedUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "No registered user exists with this email"));

        if (membershipRepository.existsByWorkspaceIdAndUserId(
                workspaceId, invitedUser.getId())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "This user is already a workspace member");
        }

        if (invitationRepository
                .existsByWorkspaceIdAndInvitedUserIdAndInvitationStatus(
                        workspaceId, invitedUser.getId(), "PENDING")) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "A pending invitation already exists for this user");
        }

        Invitation invitation = new Invitation(
                workspaceId,
                inviterUserId,
                invitedUser.getId(),
                invitedUser.getEmail());

        return toResponse(invitationRepository.saveAndFlush(invitation));
    }

    @Transactional(readOnly = true)
    public List<InvitationResponse> getMyPendingInvitations(UUID userId) {
        return invitationRepository
                .findAllByInvitedUserIdAndInvitationStatusOrderByCreatedAtDesc(
                        userId, "PENDING")
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public InvitationResponse accept(
            UUID invitationId,
            UUID authenticatedUserId) {

        Invitation invitation = invitationRepository
                .findByIdAndInvitedUserId(invitationId, authenticatedUserId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Invitation not found"));

        if (!"PENDING".equals(invitation.getInvitationStatus())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "This invitation has already been responded to");
        }

        if (membershipRepository.existsByWorkspaceIdAndUserId(
                invitation.getWorkspaceId(), authenticatedUserId)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "You are already a member of this workspace");
        }

        Membership membership = new Membership(
                invitation.getWorkspaceId(),
                authenticatedUserId,
                "MEMBER");

        membershipRepository.saveAndFlush(membership);

        invitation.accept();

        return toResponse(invitationRepository.saveAndFlush(invitation));
    }

    private InvitationResponse toResponse(Invitation invitation) {
        return new InvitationResponse(
                invitation.getId(),
                invitation.getWorkspaceId(),
                invitation.getInvitedByUserId(),
                invitation.getInvitedUserId(),
                invitation.getInvitedEmail(),
                invitation.getInvitationStatus(),
                invitation.getCreatedAt(),
                invitation.getRespondedAt());
    }
}