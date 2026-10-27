package com.mergemesh.backend.workspace;

import java.time.Instant;
import java.util.UUID;

public record WorkspaceResponse(
        UUID id,
        String name,
        UUID ownerUserId,
        String role,
        Instant createdAt
) {
}