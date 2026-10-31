CREATE TABLE device_registrations (
    id CHAR(36) NOT NULL,
    user_id CHAR(36) NOT NULL,
    device_label VARCHAR(80) NOT NULL,
    created_at TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    last_seen_at TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),

    CONSTRAINT pk_device_registrations PRIMARY KEY (id),
    CONSTRAINT fk_device_registrations_user
        FOREIGN KEY (user_id) REFERENCES users(id),
    CONSTRAINT uk_device_registrations_user_device
        UNIQUE (user_id, id),
    CONSTRAINT chk_device_label
        CHECK (CHAR_LENGTH(TRIM(device_label)) BETWEEN 1 AND 80)
) ENGINE=InnoDB;

CREATE INDEX idx_device_registrations_user
    ON device_registrations(user_id);

CREATE TABLE workspace_device_revocations (
    id CHAR(36) NOT NULL,
    workspace_id CHAR(36) NOT NULL,
    device_id CHAR(36) NOT NULL,
    revoked_by_user_id CHAR(36) NOT NULL,
    revoked_at TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),

    CONSTRAINT pk_workspace_device_revocations PRIMARY KEY (id),
    CONSTRAINT fk_device_revocations_workspace
        FOREIGN KEY (workspace_id) REFERENCES workspaces(id),
    CONSTRAINT fk_device_revocations_device
        FOREIGN KEY (device_id) REFERENCES device_registrations(id),
    CONSTRAINT fk_device_revocations_user
        FOREIGN KEY (revoked_by_user_id) REFERENCES users(id),
    CONSTRAINT uk_workspace_device_revocation
        UNIQUE (workspace_id, device_id)
) ENGINE=InnoDB;

CREATE INDEX idx_device_revocations_device
    ON workspace_device_revocations(device_id);