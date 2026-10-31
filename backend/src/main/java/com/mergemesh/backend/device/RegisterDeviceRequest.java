package com.mergemesh.backend.device;

import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record RegisterDeviceRequest(
        @NotNull(message = "Device ID is required")
        UUID deviceId,

        @NotBlank(message = "Device label is required")
        @Size(max = 80, message = "Device label must not exceed 80 characters")
        String label
) {
}