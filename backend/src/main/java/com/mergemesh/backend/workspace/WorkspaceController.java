package com.mergemesh.backend.workspace;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/workspaces")
public class WorkspaceController {

    private final WorkspaceService workspaceService;

    public WorkspaceController(WorkspaceService workspaceService) {
        this.workspaceService = workspaceService;
    }

    @PostMapping
    public ResponseEntity<WorkspaceResponse> createWorkspace(
            @Valid @RequestBody CreateWorkspaceRequest request,
            @AuthenticationPrincipal Jwt jwt) {

        UUID authenticatedUserId =
                UUID.fromString(jwt.getSubject());

        WorkspaceResponse response =
                workspaceService.createWorkspace(
                        request,
                        authenticatedUserId);

        return ResponseEntity
                .created(
                        java.net.URI.create(
                                "/api/workspaces/" + response.id()))
                .body(response);
    }
}