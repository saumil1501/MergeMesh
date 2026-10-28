package com.mergemesh.backend.invitation;

import java.time.Instant;
import java.util.UUID;

public record InvitationResponse(
        UUID id,
        UUID workspaceId,
        UUID invitedByUserId,
        UUID invitedUserId,
        String invitedEmail,
        String status,
        Instant createdAt,
        Instant respondedAt
) {
}