package com.mergemesh.backend.invitation;

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
@Table(name = "workspace_invitations")
public class Invitation {

    @Id
    @JdbcTypeCode(SqlTypes.CHAR)
    @Column(name = "id", length = 36, nullable = false, updatable = false)
    private UUID id;

    @JdbcTypeCode(SqlTypes.CHAR)
    @Column(name = "workspace_id", length = 36, nullable = false)
    private UUID workspaceId;

    @JdbcTypeCode(SqlTypes.CHAR)
    @Column(name = "invited_by_user_id", length = 36, nullable = false)
    private UUID invitedByUserId;

    @JdbcTypeCode(SqlTypes.CHAR)
    @Column(name = "invited_user_id", length = 36, nullable = false)
    private UUID invitedUserId;

    @Column(name = "invited_email", nullable = false, length = 254)
    private String invitedEmail;

    @Column(name = "invitation_status", nullable = false, length = 20)
    private String invitationStatus;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "responded_at")
    private Instant respondedAt;

    protected Invitation() {
    }

    public Invitation(
            UUID workspaceId,
            UUID invitedByUserId,
            UUID invitedUserId,
            String invitedEmail) {

        this.id = UUID.randomUUID();
        this.workspaceId = workspaceId;
        this.invitedByUserId = invitedByUserId;
        this.invitedUserId = invitedUserId;
        this.invitedEmail = invitedEmail;
        this.invitationStatus = "PENDING";
    }

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = Instant.now();
        }
    }

    public void accept() {
        this.invitationStatus = "ACCEPTED";
        this.respondedAt = Instant.now();
    }

    public void decline() {
        this.invitationStatus = "DECLINED";
        this.respondedAt = Instant.now();
    }

    public UUID getId() {
        return id;
    }

    public UUID getWorkspaceId() {
        return workspaceId;
    }

    public UUID getInvitedByUserId() {
        return invitedByUserId;
    }

    public UUID getInvitedUserId() {
        return invitedUserId;
    }

    public String getInvitedEmail() {
        return invitedEmail;
    }

    public String getInvitationStatus() {
        return invitationStatus;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getRespondedAt() {
        return respondedAt;
    }
}