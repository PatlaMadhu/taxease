package com.identityservice.exception;

/**
 * Thrown when a security answer verification fails during forgot-password flow.
 * errorCode values: WRONG_ANSWER, EMAIL_NOT_FOUND, SECURITY_ANSWER_NOT_SET
 */
public class SecurityAnswerException extends RuntimeException {
    private final String errorCode;

    public SecurityAnswerException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }
}
