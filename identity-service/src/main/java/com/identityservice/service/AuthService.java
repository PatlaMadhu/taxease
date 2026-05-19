package com.identityservice.service;

import com.identityservice.dto.requestdto.ForgotPasswordRequestDto;
import com.identityservice.dto.requestdto.LoginRequestDto;
import com.identityservice.dto.requestdto.ResetPasswordRequestDto;
import com.identityservice.dto.responsedto.LoginResponseDto;

public interface AuthService {
    LoginResponseDto login(LoginRequestDto dto);

    /**
     * Verifies email + security answer.
     * Returns a reset token if correct; throws IllegalArgumentException if answer is wrong.
     */
    String forgotPassword(ForgotPasswordRequestDto dto);

    void resetPassword(ResetPasswordRequestDto dto);
}
