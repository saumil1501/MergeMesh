package com.mergemesh.backend.workspace;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface WorkspaceRepository
        extends JpaRepository<Workspace, UUID> {

    @Query("""
            select w
            from Workspace w, Membership m
            where m.workspaceId = w.id
              and m.userId = :userId
            order by m.joinedAt desc
            """)
    List<Workspace> findAllForUser(
            @Param("userId") UUID userId);

    @Query("""
            select w
            from Workspace w, Membership m
            where w.id = :workspaceId
              and m.workspaceId = w.id
              and m.userId = :userId
            """)
    Optional<Workspace> findAccessibleWorkspace(
            @Param("workspaceId") UUID workspaceId,
            @Param("userId") UUID userId);
}