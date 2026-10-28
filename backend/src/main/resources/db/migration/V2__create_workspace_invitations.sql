CREATE TABLE workspace_invitations (
    id CHAR(36) NOT NULL,
    workspace_id CHAR(36) NOT NULL,
    invited_by_user_id CHAR(36) NOT NULL,
    invited_user_id CHAR(36) NOT NULL,
    invited_email VARCHAR(254) NOT NULL,
    invitation_status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    created_at TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    responded_at TIMESTAMP(6) NULL,

    CONSTRAINT pk_workspace_invitations
        PRIMARY KEY (id),

    CONSTRAINT fk_invitations_workspace
        FOREIGN KEY (workspace_id) REFERENCES workspaces(id),

    CONSTRAINT fk_invitations_inviter
        FOREIGN KEY (invited_by_user_id) REFERENCES users(id),

    CONSTRAINT fk_invitations_invited_user
        FOREIGN KEY (invited_user_id) REFERENCES users(id),

    CONSTRAINT chk_invitation_status
        CHECK (invitation_status IN ('PENDING', 'ACCEPTED', 'DECLINED'))
) ENGINE=InnoDB;

CREATE INDEX idx_invitations_invited_user_status
    ON workspace_invitations(invited_user_id, invitation_status);

CREATE INDEX idx_invitations_workspace_status
    ON workspace_invitations(workspace_id, invitation_status);