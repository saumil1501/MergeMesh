package com.mergemesh.backend.device;

import java.time.Instant;
import java.util.UUID;

public record DeviceResponse(
        UUID deviceId,
        UUID userId,
        String label,
        Instant createdAt,
        Instant lastSeenAt,
        boolean revoked
) {
}