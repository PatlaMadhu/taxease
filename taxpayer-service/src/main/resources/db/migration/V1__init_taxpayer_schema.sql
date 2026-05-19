CREATE TABLE IF NOT EXISTS taxpayer (
    taxpayer_id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id             BIGINT          NOT NULL UNIQUE,
    name                VARCHAR(100),
    email               VARCHAR(255)    NOT NULL UNIQUE,
    phone               VARCHAR(30),
    taxpayer_id_number  VARCHAR(50)     UNIQUE,
    pan_number          VARCHAR(20),
    type                VARCHAR(30),
    address             TEXT,
    contact_info        TEXT,
    created_at          DATETIME(6)     NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at          DATETIME(6)     NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
    INDEX idx_taxpayer_user (user_id),
    INDEX idx_taxpayer_email (email)
);

CREATE TABLE IF NOT EXISTS taxpayer_document (
    document_id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    taxpayer_id         BIGINT          NOT NULL,
    doc_type            VARCHAR(50)     NOT NULL,
    file_uri            TEXT            NOT NULL,
    verification_status VARCHAR(30)     NOT NULL DEFAULT 'Pending',
    uploaded_date       DATETIME(6)     NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at          DATETIME(6)     NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
    INDEX idx_doc_taxpayer (taxpayer_id),
    CONSTRAINT fk_doc_taxpayer FOREIGN KEY (taxpayer_id) REFERENCES taxpayer(taxpayer_id)
);
