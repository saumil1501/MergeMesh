package com.mergemesh.backend.device;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.mergemesh.backend.user.User;
import com.mergemesh.backend.user.UserRepository;
import com.mergemesh.backend.workspace.Membership;
import com.mergemesh.backend.workspace.MembershipRepository;
import com.mergemesh.backend.workspace.Workspace;
import com.mergemesh.backend.workspace.WorkspaceRepository;

@Service
public class DeviceService {

    private final DeviceRegistrationRepository deviceRepository;
    private final DeviceRevocationRepository revocationRepository;
    private final WorkspaceRepository workspaceRepository;
    private final MembershipRepository membershipRepository;
    private final UserRepository userRepository;

    public DeviceService(
            DeviceRegistrationRepository deviceRepository,
            DeviceRevocationRepository revocationRepository,
            WorkspaceRepository workspaceRepository,
            MembershipRepository membershipRepository,
            UserRepository userRepository) {

        this.deviceRepository = deviceRepository;
        this.revocationRepository = revocationRepository;
        this.workspaceRepository = workspaceRepository;
        this.membershipRepository = membershipRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public DeviceResponse register(
            RegisterDeviceRequest request,
            UUID userId) {

        String label = request.label().trim();

        if (label.isBlank() || label.length() > 80) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Device label must contain 1 to 80 characters");
        }

        return deviceRepository.findById(request.deviceId())
                .map(existing -> {
                    if (!existing.getUserId().equals(userId)) {
                        throw new ResponseStatusException(
                                HttpStatus.CONFLICT,
                                "This device ID is already registered to another user");
                    }

                    existing.updateLabel(label);
                    existing.markSeen();

                    return toResponse(
                            deviceRepository.save(existing), false);
                })
                .orElseGet(() -> {
                    DeviceRegistration device =
                            new DeviceRegistration(
                                    request.deviceId(), userId, label);

                    return toResponse(
                            deviceRepository.saveAndFlush(device), false);
                });
    }

    @Transactional(readOnly = true)
    public List<DeviceResponse> getMyDevices(UUID userId) {

        return deviceRepository
                .findAllByUserIdOrderByLastSeenAtDesc(userId)
                .stream()
                .map(device -> toResponse(device, false))
                .toList();
    }

    @Transactional
    public DeviceResponse updateLabel(
            UUID deviceId,
            UUID userId,
            UpdateDeviceLabelRequest request) {

        DeviceRegistration device = deviceRepository
                .findByIdAndUserId(deviceId, userId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Device not found"));

        String label = request.label().trim();

        if (label.isBlank() || label.length() > 80) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Device label must contain 1 to 80 characters");
        }

        device.updateLabel(label);
        device.markSeen();

        return toResponse(deviceRepository.save(device), false);
    }

    @Transactional(readOnly = true)
    public List<DeviceResponse> getWorkspaceDevices(
            UUID workspaceId,
            UUID requesterId) {

        Workspace workspace = getWorkspace(workspaceId);

        if (!workspace.getOwnerUserId().equals(requesterId)) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Only the workspace creator can view all registered devices");
        }

        List<UUID> memberIds = membershipRepository
                .findAllByWorkspaceId(workspaceId)
                .stream()
                .map(Membership::getUserId)
                .toList();

        return deviceRepository
                .findAllByUserIdInOrderByLastSeenAtDesc(memberIds)
                .stream()
                .map(device -> toResponse(
                        device,
                        revocationRepository.existsByWorkspaceIdAndDeviceId(
                                workspaceId, device.getId())))
                .toList();
    }

    @Transactional
    public RevokeDeviceResponse revoke(
            UUID workspaceId,
            UUID deviceId,
            UUID requesterId) {

        Workspace workspace = getWorkspace(workspaceId);

        if (!workspace.getOwnerUserId().equals(requesterId)) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Only the workspace creator can revoke devices");
        }

        DeviceRegistration device = deviceRepository.findById(deviceId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Device not found"));

        boolean isWorkspaceMember = membershipRepository
                .existsByWorkspaceIdAndUserId(
                        workspaceId, device.getUserId());

        if (!isWorkspaceMember) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Device does not belong to a member of this workspace");
        }

        if (revocationRepository.existsByWorkspaceIdAndDeviceId(
                workspaceId, deviceId)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Device is already revoked in this workspace");
        }

        DeviceRevocation revocation = new DeviceRevocation(
                workspaceId, deviceId, requesterId);

        DeviceRevocation saved =
                revocationRepository.saveAndFlush(revocation);

        return new RevokeDeviceResponse(
                saved.getWorkspaceId(),
                saved.getDeviceId(),
                saved.getRevokedByUserId(),
                saved.getRevokedAt());
    }

    private Workspace getWorkspace(UUID workspaceId) {
        return workspaceRepository.findById(workspaceId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Workspace not found"));
    }

    private DeviceResponse toResponse(
            DeviceRegistration device,
            boolean revoked) {

        return new DeviceResponse(
                device.getId(),
                device.getUserId(),
                device.getDeviceLabel(),
                device.getCreatedAt(),
                device.getLastSeenAt(),
                revoked);
    }
}