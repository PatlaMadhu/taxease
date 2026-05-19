CREATE TABLE IF NOT EXISTS tax_filing (
    filing_id        BIGINT AUTO_INCREMENT PRIMARY KEY,
    taxpayer_id      BIGINT          NOT NULL,
    user_id          BIGINT,
    taxpayer_email   VARCHAR(255)    NOT NULL,
    period           VARCHAR(20)     NOT NULL,
    amount_declared  DECIMAL(15, 2)  NOT NULL,
    status           VARCHAR(30)     NOT NULL DEFAULT 'DRAFT',
    submitted_date   DATETIME(6)     NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at       DATETIME(6)     NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
    INDEX idx_filing_taxpayer (taxpayer_id),
    INDEX idx_filing_status (status)
);

CREATE TABLE IF NOT EXISTS filing_document (
    document_id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    filing_id           BIGINT        NOT NULL,
    doc_type            VARCHAR(50)   NOT NULL,
    file_uri            TEXT          NOT NULL,
    verification_status VARCHAR(30)   NOT NULL DEFAULT 'PENDING',
    uploaded_date       DATETIME(6)   NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at          DATETIME(6)   NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
    CONSTRAINT fk_filing_doc FOREIGN KEY (filing_id) REFERENCES tax_filing(filing_id)
);
