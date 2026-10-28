package com.mergemesh.backend.invitation;

import java.net.URI;
import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
public class InvitationController {

    private final InvitationService invitationService;

    public InvitationController(InvitationService invitationService) {
        this.invitationService = invitationService;
    }

    @PostMapping("/workspaces/{workspaceId}/invitations")
    public ResponseEntity<InvitationResponse> invite(
            @PathVariable UUID workspaceId,
            @Valid @RequestBody CreateInvitationRequest request,
            @AuthenticationPrincipal Jwt jwt) {

        UUID inviterUserId = UUID.fromString(jwt.getSubject());

        InvitationResponse response = invitationService.invite(
                workspaceId, inviterUserId, request);

        return ResponseEntity
                .created(URI.create("/api/workspaces/" + workspaceId
                        + "/invitations/" + response.id()))
                .body(response);
    }

    @GetMapping("/workspaces/invitations")
    public ResponseEntity<List<InvitationResponse>> getMyInvitations(
            @AuthenticationPrincipal Jwt jwt) {

        UUID userId = UUID.fromString(jwt.getSubject());

        return ResponseEntity.ok(
                invitationService.getMyPendingInvitations(userId));
    }

    @PostMapping("/workspaces/invitations/{invitationId}/accept")
    public ResponseEntity<InvitationResponse> accept(
            @PathVariable UUID invitationId,
            @AuthenticationPrincipal Jwt jwt) {

        UUID userId = UUID.fromString(jwt.getSubject());

        return ResponseEntity.ok(
                invitationService.accept(invitationId, userId));
    }
}