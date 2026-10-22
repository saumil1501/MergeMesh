
CREATE TABLE users (
    id CHAR(36) NOT NULL,
    display_name VARCHAR(80) NOT NULL,
    email VARCHAR(254) NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    created_at TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6)
        ON UPDATE CURRENT_TIMESTAMP(6),

    CONSTRAINT pk_users PRIMARY KEY (id),
    CONSTRAINT uk_users_email UNIQUE (email),
    CONSTRAINT chk_users_display_name
        CHECK (CHAR_LENGTH(TRIM(display_name)) BETWEEN 1 AND 80)
) ENGINE=InnoDB;

CREATE TABLE workspaces (
    id CHAR(36) NOT NULL,
    name VARCHAR(60) NOT NULL,
    owner_user_id CHAR(36) NOT NULL,
    created_at TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6)
        ON UPDATE CURRENT_TIMESTAMP(6),

    CONSTRAINT pk_workspaces PRIMARY KEY (id),
    CONSTRAINT fk_workspaces_owner
        FOREIGN KEY (owner_user_id) REFERENCES users(id),
    CONSTRAINT chk_workspaces_name
        CHECK (CHAR_LENGTH(TRIM(name)) BETWEEN 3 AND 60)
) ENGINE=InnoDB;

CREATE TABLE memberships (
    id CHAR(36) NOT NULL,
    workspace_id CHAR(36) NOT NULL,
    user_id CHAR(36) NOT NULL,
    membership_role VARCHAR(20) NOT NULL,
    joined_at TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),

    CONSTRAINT pk_memberships PRIMARY KEY (id),
    CONSTRAINT uk_memberships_workspace_user
        UNIQUE (workspace_id, user_id),
    CONSTRAINT fk_memberships_workspace
        FOREIGN KEY (workspace_id) REFERENCES workspaces(id),
    CONSTRAINT fk_memberships_user
        FOREIGN KEY (user_id) REFERENCES users(id),
    CONSTRAINT chk_memberships_role
        CHECK (membership_role IN ('OWNER', 'MEMBER'))
) ENGINE=InnoDB;

CREATE INDEX idx_memberships_user
    ON memberships(user_id);
