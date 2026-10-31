package com.mergemesh.backend.device;

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
@Table(name = "workspace_device_revocations")
public class DeviceRevocation {

    @Id
    @JdbcTypeCode(SqlTypes.CHAR)
    @Column(length = 36, nullable = false, updatable = false)
    private UUID id;

    @JdbcTypeCode(SqlTypes.CHAR)
    @Column(name = "workspace_id", length = 36, nullable = false)
    private UUID workspaceId;

    @JdbcTypeCode(SqlTypes.CHAR)
    @Column(name = "device_id", length = 36, nullable = false)
    private UUID deviceId;

    @JdbcTypeCode(SqlTypes.CHAR)
    @Column(name = "revoked_by_user_id", length = 36, nullable = false)
    private UUID revokedByUserId;

    @Column(name = "revoked_at", nullable = false, updatable = false)
    private Instant revokedAt;

    protected DeviceRevocation() {
    }

    public DeviceRevocation(
            UUID workspaceId,
            UUID deviceId,
            UUID revokedByUserId) {
        this.id = UUID.randomUUID();
        this.workspaceId = workspaceId;
        this.deviceId = deviceId;
        this.revokedByUserId = revokedByUserId;
    }

    @PrePersist
    protected void onCreate() {
        if (revokedAt == null) {
            revokedAt = Instant.now();
        }
    }

    public UUID getId() { return id; }
    public UUID getWorkspaceId() { return workspaceId; }
    public UUID getDeviceId() { return deviceId; }
    public UUID getRevokedByUserId() { return revokedByUserId; }
    public Instant getRevokedAt() { return revokedAt; }
}