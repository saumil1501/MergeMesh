package com.mergemesh.backend.workspace;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class WorkspaceService {

    private final WorkspaceRepository workspaceRepository;
    private final MembershipRepository membershipRepository;

    public WorkspaceService(
            WorkspaceRepository workspaceRepository,
            MembershipRepository membershipRepository) {

        this.workspaceRepository = workspaceRepository;
        this.membershipRepository = membershipRepository;
    }

    @Transactional
    public WorkspaceResponse createWorkspace(
            CreateWorkspaceRequest request,
            UUID ownerUserId) {

        String name = request.name().trim();

        if (name.length() < 3 || name.length() > 60) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Workspace name must be between 3 and 60 characters");
        }

        Workspace workspace = new Workspace(name, ownerUserId);

        Workspace savedWorkspace =
                workspaceRepository.saveAndFlush(workspace);

        Membership ownerMembership = new Membership(
                savedWorkspace.getId(),
                ownerUserId,
                "OWNER");

        membershipRepository.saveAndFlush(ownerMembership);

        return new WorkspaceResponse(
                savedWorkspace.getId(),
                savedWorkspace.getName(),
                savedWorkspace.getOwnerUserId(),
                ownerMembership.getMembershipRole(),
                savedWorkspace.getCreatedAt());
    }
}