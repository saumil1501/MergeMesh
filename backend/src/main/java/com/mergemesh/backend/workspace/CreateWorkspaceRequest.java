package com.mergemesh.backend.workspace;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateWorkspaceRequest(
        @NotBlank(message = "Workspace name is required")
        @Size(min = 3, max = 60,
              message = "Workspace name must be between 3 and 60 characters")
        String name
) {
}