CREATE TABLE IF NOT EXISTS audit_case (
    audit_id   BIGINT AUTO_INCREMENT PRIMARY KEY,
    officer_id BIGINT        NOT NULL,
    taxpayer_id BIGINT,
    scope      VARCHAR(200),
    findings   LONGTEXT,
    status     VARCHAR(30)   NOT NULL DEFAULT 'OPEN',
    created_at DATETIME(6)   NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at DATETIME(6)   NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
    INDEX idx_audit_officer (officer_id)
);

CREATE TABLE IF NOT EXISTS compliance_record (
    compliance_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    entity_id     BIGINT        NOT NULL,
    type          VARCHAR(30)   NOT NULL,
    result        VARCHAR(100)  NOT NULL,
    notes         TEXT,
    record_date   DATETIME(6)   NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    INDEX idx_compliance_entity (entity_id)
);
