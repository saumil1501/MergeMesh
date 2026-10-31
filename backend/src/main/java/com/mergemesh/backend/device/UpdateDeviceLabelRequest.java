package com.mergemesh.backend.device;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateDeviceLabelRequest(
        @NotBlank(message = "Device label is required")
        @Size(max = 80, message = "Device label must not exceed 80 characters")
        String label
) {
}