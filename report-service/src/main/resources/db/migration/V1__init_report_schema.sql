CREATE TABLE IF NOT EXISTS report (
    report_id      BIGINT AUTO_INCREMENT PRIMARY KEY,
    scope          VARCHAR(30)   NOT NULL,
    title          VARCHAR(255)  NOT NULL,
    metrics        TEXT,
    generated_by   BIGINT,
    generated_date DATETIME(6)   NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    INDEX idx_report_scope (scope)
);
