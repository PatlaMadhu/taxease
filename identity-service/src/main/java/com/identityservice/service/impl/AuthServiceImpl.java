package com.identityservice.service.impl;

import com.identityservice.dao.PasswordResetTokenRepository;
import com.identityservice.dao.UserRepository;
import com.identityservice.dto.requestdto.ForgotPasswordRequestDto;
import com.identityservice.dto.requestdto.LoginRequestDto;
import com.identityservice.dto.requestdto.ResetPasswordRequestDto;
import com.identityservice.dto.responsedto.LoginResponseDto;
import com.identityservice.entity.PasswordResetToken;
import com.identityservice.entity.User;
import com.identityservice.exception.SecurityAnswerException;
import com.identityservice.service.AuthService;
import com.identityservice.service.JwtService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final PasswordResetTokenRepository resetTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final Optional<JavaMailSender> mailSender;

    @Value("${spring.mail.username:}")
    private String fromEmail;

    @Autowired
    public AuthServiceImpl(AuthenticationManager authenticationManager,
                           JwtService jwtService,
                           UserRepository userRepository,
                           PasswordResetTokenRepository resetTokenRepository,
                           PasswordEncoder passwordEncoder,
                           Optional<JavaMailSender> mailSender) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userRepository = userRepository;
        this.resetTokenRepository = resetTokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.mailSender = mailSender;
    }

    @Override
    public LoginResponseDto login(LoginRequestDto dto) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword())
        );
        User user = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new NoSuchElementException("User not found"));
        String token = jwtService.generateToken(user.getEmail(), user.getRole().name(), user.getId());
        return new LoginResponseDto(token);
    }

    /**
     * Forgot-password with security question verification.
     * 1. Find user by email (throws SecurityAnswerException EMAIL_NOT_FOUND if absent).
     * 2. Verify supplied security answer against stored BCrypt hash.
     * 3. If wrong → throws SecurityAnswerException WRONG_ANSWER (caught by GlobalExceptionHandler,
     *    returns HTTP 400 with errorCode so Angular can show "Wrong answer. Try again.").
     * 4. If correct → generate reset token and return it.
     */
    @Override
    @Transactional
    public String forgotPassword(ForgotPasswordRequestDto dto) {
        User user = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new SecurityAnswerException("EMAIL_NOT_FOUND",
                        "No account found with that email address."));

        if (user.getSecurityAnswerHash() == null || user.getSecurityAnswerHash().isBlank()) {
            throw new SecurityAnswerException("SECURITY_ANSWER_NOT_SET",
                    "This account does not have a security answer set. Please contact support.");
        }

        // Normalize to lowercase (case-insensitive comparison)
        String provided = dto.getSecurityAnswer() == null ? "" : dto.getSecurityAnswer().trim().toLowerCase();

        if (!passwordEncoder.matches(provided, user.getSecurityAnswerHash())) {
            log.warn("Wrong security answer attempt for email: {}", dto.getEmail());
            throw new SecurityAnswerException("WRONG_ANSWER", "Wrong answer. Please try again.");
        }

        // Correct answer — issue reset token
        String token = UUID.randomUUID().toString();
        resetTokenRepository.save(PasswordResetToken.builder()
                .token(token)
                .user(user)
                .expiresAt(Instant.now().plusSeconds(3600))
                .build());

        SimpleMailMessage mail = new SimpleMailMessage();
        if (fromEmail != null && !fromEmail.isBlank()) mail.setFrom(fromEmail);
        mail.setTo(user.getEmail());
        mail.setSubject("TaxEase - Password Reset Request");
        mail.setText("Hello " + user.getName() + ",\n\n"
                + "You requested a password reset for your TaxEase account.\n\n"
                + "Your reset token (valid for 1 hour):\n\n"
                + token + "\n\n"
                + "Use this token on the Reset Password page to set a new password.\n\n"
                + "If you did not request this, please ignore this email.\n\n"
                + "- TaxEase Team");

        mailSender.ifPresent(sender -> {
            if (fromEmail == null || fromEmail.isBlank()) {
                log.info("Mail not configured — reset token for {}: {}", user.getEmail(), token);
                return;
            }
            CompletableFuture.runAsync(() -> {
                try {
                    sender.send(mail);
                    log.info("Password reset email sent to: {}", user.getEmail());
                } catch (Exception e) {
                    log.warn("Could not send reset email to {} (token still saved): {}", user.getEmail(), e.getMessage());
                }
            });
        });

        return token;
    }

    @Override
    @Transactional
    public void resetPassword(ResetPasswordRequestDto dto) {
        PasswordResetToken resetToken = resetTokenRepository.findByToken(dto.getToken())
                .orElseThrow(() -> new IllegalArgumentException("Invalid or expired reset token"));
        if (resetToken.isUsed() || resetToken.getExpiresAt().isBefore(Instant.now())) {
            throw new IllegalArgumentException("Invalid or expired reset token");
        }
        User user = resetToken.getUser();
        user.setPasswordHash(passwordEncoder.encode(dto.getNewPassword()));
        user.setPasswordChangedAt(Instant.now());
        userRepository.save(user);
        resetToken.setUsed(true);
        resetTokenRepository.save(resetToken);
        log.info("Password reset successful for user: {}", user.getEmail());
    }
}
