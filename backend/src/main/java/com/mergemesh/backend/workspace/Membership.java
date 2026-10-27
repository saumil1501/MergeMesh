package com.mergemesh.backend.workspace;

import java.time.Instant;
import java.util.UUID;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

@Entity
@Table(name = "memberships")
public class Membership {

    @Id
    @JdbcTypeCode(SqlTypes.CHAR)
    @Column(name = "id", length = 36, nullable = false, updatable = false)
    private UUID id;

    @JdbcTypeCode(SqlTypes.CHAR)
    @Column(name = "workspace_id", length = 36, nullable = false)
    private UUID workspaceId;

    @JdbcTypeCode(SqlTypes.CHAR)
    @Column(name = "user_id", length = 36, nullable = false)
    private UUID userId;

    @Column(name = "membership_role", nullable = false, length = 20)
    private String membershipRole;

    @Column(name = "joined_at", nullable = false, updatable = false)
    private Instant joinedAt;

    protected Membership() {
    }

    public Membership(
            UUID workspaceId,
            UUID userId,
            String membershipRole) {

        this.id = UUID.randomUUID();
        this.workspaceId = workspaceId;
        this.userId = userId;
        this.membershipRole = membershipRole;
    }

    @PrePersist
    protected void onCreate() {
        if (joinedAt == null) {
            joinedAt = Instant.now();
        }
    }

    public UUID getId() {
        return id;
    }

    public UUID getWorkspaceId() {
        return workspaceId;
    }

    public UUID getUserId() {
        return userId;
    }

    public String getMembershipRole() {
        return membershipRole;
    }

    public Instant getJoinedAt() {
        return joinedAt;
    }
}