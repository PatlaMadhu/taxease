CREATE TABLE IF NOT EXISTS notification (
    notification_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id         BIGINT        NOT NULL,
    entity_id       BIGINT,
    message         TEXT          NOT NULL,
    category        VARCHAR(30)   NOT NULL,
    status          VARCHAR(20)   NOT NULL DEFAULT 'UNREAD',
    created_date    DATETIME(6)   NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    INDEX idx_notification_user (user_id),
    INDEX idx_notification_status (status)
);
