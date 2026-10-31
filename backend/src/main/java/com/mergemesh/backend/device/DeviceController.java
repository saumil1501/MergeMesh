package com.mergemesh.backend.device;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
public class DeviceController {

    private final DeviceService deviceService;

    public DeviceController(DeviceService deviceService) {
        this.deviceService = deviceService;
    }

    @PostMapping("/devices/register")
    public ResponseEntity<DeviceResponse> register(
            @Valid @RequestBody RegisterDeviceRequest request,
            @AuthenticationPrincipal Jwt jwt) {

        UUID userId = UUID.fromString(jwt.getSubject());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(deviceService.register(request, userId));
    }

    @GetMapping("/devices")
    public ResponseEntity<List<DeviceResponse>> getMyDevices(
            @AuthenticationPrincipal Jwt jwt) {

        UUID userId = UUID.fromString(jwt.getSubject());

        return ResponseEntity.ok(
                deviceService.getMyDevices(userId));
    }

    @PatchMapping("/devices/{deviceId}/label")
    public ResponseEntity<DeviceResponse> updateLabel(
            @PathVariable UUID deviceId,
            @Valid @RequestBody UpdateDeviceLabelRequest request,
            @AuthenticationPrincipal Jwt jwt) {

        UUID userId = UUID.fromString(jwt.getSubject());

        return ResponseEntity.ok(
                deviceService.updateLabel(deviceId, userId, request));
    }

    @GetMapping("/workspaces/{workspaceId}/devices")
    public ResponseEntity<List<DeviceResponse>> getWorkspaceDevices(
            @PathVariable UUID workspaceId,
            @AuthenticationPrincipal Jwt jwt) {

        UUID userId = UUID.fromString(jwt.getSubject());

        return ResponseEntity.ok(
                deviceService.getWorkspaceDevices(workspaceId, userId));
    }

    @PostMapping("/workspaces/{workspaceId}/devices/{deviceId}/revoke")
    public ResponseEntity<RevokeDeviceResponse> revoke(
            @PathVariable UUID workspaceId,
            @PathVariable UUID deviceId,
            @AuthenticationPrincipal Jwt jwt) {

        UUID userId = UUID.fromString(jwt.getSubject());

        return ResponseEntity.ok(
                deviceService.revoke(workspaceId, deviceId, userId));
    }
}