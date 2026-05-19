package com.identityservice.controller;

import com.identityservice.dto.requestdto.ForgotPasswordRequestDto;
import com.identityservice.dto.requestdto.LoginRequestDto;
import com.identityservice.dto.requestdto.ResetPasswordRequestDto;
import com.identityservice.dto.requestdto.TaxpayerRegistrationRequestDto;
import com.identityservice.dto.responsedto.LoginResponseDto;
import com.identityservice.dto.responsedto.TaxpayerRegistrationResponseDto;
import com.identityservice.service.AuthService;
import com.identityservice.service.JwtService;
import com.identityservice.service.TaxpayerRegistrationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
@Validated
public class AuthController {

    private final TaxpayerRegistrationService registrationService;
    private final AuthService authService;
    private final JwtService jwtService;

    @PostMapping("/register")
    public ResponseEntity<TaxpayerRegistrationResponseDto> register(
            @Valid @RequestBody TaxpayerRegistrationRequestDto request) {
        log.info("START: Registering taxpayer with email: {}", request.getEmail());
        TaxpayerRegistrationResponseDto response = registrationService.registerTaxpayer(request);
        log.info("END: Registration successful, taxId: {}", response.getTaxpayerIdNumber());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(
            @Valid @RequestBody LoginRequestDto request) {
        log.info("START: Login attempt for: {}", request.getEmail());
        LoginResponseDto response = authService.login(request);
        log.info("END: Login successful");
        return ResponseEntity.ok(response);
    }

    /**
     * Forgot-password endpoint.
     * Requires email + securityAnswer in body.
     * SecurityAnswerException is handled by GlobalExceptionHandler with proper errorCode.
     * On success: returns 200 with { message, resetToken }.
     */
    @PostMapping("/forgot-password")
    public ResponseEntity<Map<String, String>> forgotPassword(
            @Valid @RequestBody ForgotPasswordRequestDto request) {
        String token = authService.forgotPassword(request);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Identity verified. You may now reset your password.");
        if (token != null) response.put("resetToken", token);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<Map<String, String>> resetPassword(
            @Valid @RequestBody ResetPasswordRequestDto request) {
        authService.resetPassword(request);
        return ResponseEntity.ok(Map.of("message", "Password reset successful."));
    }

    /** Called by API Gateway to validate incoming JWT tokens */
    @GetMapping("/validate")
    public ResponseEntity<Map<String, String>> validate(@RequestParam String token) {
        log.info("START: Validating token");
        jwtService.validateToken(token);
        String email = jwtService.extractEmail(token);
        String role = jwtService.extractRole(token);
        log.info("END: Token valid for email: {}", email);
        return ResponseEntity.ok(Map.of("email", email, "role", role));
    }
}
