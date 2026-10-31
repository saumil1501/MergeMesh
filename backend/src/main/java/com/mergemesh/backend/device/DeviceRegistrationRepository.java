package com.mergemesh.backend.device;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface DeviceRegistrationRepository
        extends JpaRepository<DeviceRegistration, UUID> {

    Optional<DeviceRegistration> findByIdAndUserId(
            UUID id, UUID userId);

    List<DeviceRegistration> findAllByUserIdOrderByLastSeenAtDesc(
            UUID userId);

    List<DeviceRegistration> findAllByUserIdInOrderByLastSeenAtDesc(
            List<UUID> userIds);
}