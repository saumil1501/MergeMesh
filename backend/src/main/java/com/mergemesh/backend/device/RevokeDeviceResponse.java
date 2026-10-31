package com.mergemesh.backend.device;

import java.time.Instant;
import java.util.UUID;

public record RevokeDeviceResponse(
        UUID workspaceId,
        UUID deviceId,
        UUID revokedByUserId,
        Instant revokedAt
) {
}