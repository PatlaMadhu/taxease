CREATE TABLE IF NOT EXISTS `user` (
    user_id             BIGINT AUTO_INCREMENT PRIMARY KEY,
    name                VARCHAR(200)  NOT NULL,
    email               VARCHAR(255)  NOT NULL UNIQUE,
    phone               VARCHAR(30),
    password_hash       VARCHAR(255)  NOT NULL,
    security_answer_hash VARCHAR(255),
    role                VARCHAR(40)   NOT NULL,
    status              VARCHAR(30)   NOT NULL DEFAULT 'Active',
    password_changed_at DATETIME(6),
    created_at          DATETIME(6)   NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at          DATETIME(6)   NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
    INDEX idx_user_email (email),
    INDEX idx_user_role_status (role, status)
);

CREATE TABLE IF NOT EXISTS taxpayer (
    taxpayer_id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id             BIGINT        NOT NULL UNIQUE,
    name                VARCHAR(100),
    taxpayer_id_number  VARCHAR(50)   UNIQUE,
    type                VARCHAR(30),
    address             TEXT,
    contact_info        TEXT,
    created_at          DATETIME(6)   NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at          DATETIME(6)   NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
    CONSTRAINT fk_taxpayer_user FOREIGN KEY (user_id) REFERENCES `user`(user_id)
);

CREATE TABLE IF NOT EXISTS audit_log (
    audit_log_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id      BIGINT        NOT NULL,
    action       VARCHAR(100)  NOT NULL,
    resource     VARCHAR(200)  NOT NULL,
    timestamp    DATETIME(6)   NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    INDEX idx_auditlog_user (user_id),
    INDEX idx_auditlog_ts (timestamp),
    INDEX idx_auditlog_action_ts (action, timestamp),
    CONSTRAINT fk_auditlog_user FOREIGN KEY (user_id) REFERENCES `user`(user_id)
);
