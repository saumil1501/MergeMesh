package com.mergemesh.backend.workspace;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MembershipRepository
        extends JpaRepository<Membership, UUID> {

    boolean existsByWorkspaceIdAndUserId(
            UUID workspaceId,
            UUID userId);

    List<Membership> findAllByUserIdOrderByJoinedAtDesc(
            UUID userId);

    Optional<Membership> findByWorkspaceIdAndUserId(
            UUID workspaceId,
            UUID userId);
}