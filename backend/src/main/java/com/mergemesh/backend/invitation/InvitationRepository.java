package com.mergemesh.backend.invitation;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface InvitationRepository
        extends JpaRepository<Invitation, UUID> {

    boolean existsByWorkspaceIdAndInvitedUserIdAndInvitationStatus(
            UUID workspaceId,
            UUID invitedUserId,
            String invitationStatus);

    List<Invitation> findAllByInvitedUserIdAndInvitationStatusOrderByCreatedAtDesc(
            UUID invitedUserId,
            String invitationStatus);

    Optional<Invitation> findByIdAndInvitedUserId(
            UUID id,
            UUID invitedUserId);
}