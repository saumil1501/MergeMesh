
package com.mergemesh.backend.auth;

import java.time.Instant;
import java.util.UUID;

public record LoginResponse(
        String accessToken,
        String tokenType,
        Instant expiresAt,
        UUID userId,
        String displayName,
        String email
) {
}
