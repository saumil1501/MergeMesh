package com.mergemesh.backend.device;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface DeviceRevocationRepository
        extends JpaRepository<DeviceRevocation, UUID> {

    boolean existsByWorkspaceIdAndDeviceId(
            UUID workspaceId, UUID deviceId);

    Optional<DeviceRevocation> findByWorkspaceIdAndDeviceId(
            UUID workspaceId, UUID deviceId);

    List<DeviceRevocation> findAllByWorkspaceId(UUID workspaceId);
}