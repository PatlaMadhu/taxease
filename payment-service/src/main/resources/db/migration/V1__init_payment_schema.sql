CREATE TABLE IF NOT EXISTS payment (
    payment_id   BIGINT AUTO_INCREMENT PRIMARY KEY,
    filing_id    BIGINT          NOT NULL,
    taxpayer_id  BIGINT          NOT NULL,
    amount       DECIMAL(15, 2)  NOT NULL,
    method       VARCHAR(20)     NOT NULL,
    status       VARCHAR(20)     NOT NULL DEFAULT 'Pending',
    payment_date DATETIME(6)     NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at   DATETIME(6)     NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
    INDEX idx_payment_filing (filing_id),
    INDEX idx_payment_status (status)
);

CREATE TABLE IF NOT EXISTS revenue_record (
    revenue_id   BIGINT AUTO_INCREMENT PRIMARY KEY,
    payment_id   BIGINT          NOT NULL,
    taxpayer_id  BIGINT          NOT NULL,
    amount       DECIMAL(15, 2)  NOT NULL,
    status       VARCHAR(30)     NOT NULL DEFAULT 'PENDING',
    record_date  DATETIME(6)     NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    INDEX idx_revenue_taxpayer (taxpayer_id)
);
