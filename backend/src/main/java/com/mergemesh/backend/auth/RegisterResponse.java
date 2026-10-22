
package com.mergemesh.backend.auth;

import java.util.UUID;

public record RegisterResponse(
        UUID id,
        String displayName,
        String email
) {
}
