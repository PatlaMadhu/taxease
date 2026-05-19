-- V2: Add security_answer_hash column to user table
-- Question is fixed: "What is your favorite place?"
ALTER TABLE `user`
    ADD COLUMN IF NOT EXISTS security_answer_hash VARCHAR(255) NULL
        COMMENT 'BCrypt hash of answer to fixed security question: What is your favorite place?';
