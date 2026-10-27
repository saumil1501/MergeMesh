package com.mergemesh.backend.workspace;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MembershipRepository
        extends JpaRepository<Membership, UUID> {

    boolean existsByWorkspaceIdAndUserId(
            UUID workspaceId,
            UUID userId);
}