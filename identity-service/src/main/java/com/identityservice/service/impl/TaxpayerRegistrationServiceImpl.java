package com.identityservice.service.impl;

import com.identityservice.dao.TaxpayerRepository;
import com.identityservice.dao.UserRepository;
import com.identityservice.dto.requestdto.TaxpayerRegistrationRequestDto;
import com.identityservice.dto.responsedto.TaxpayerRegistrationResponseDto;
import com.identityservice.entity.Taxpayer;
import com.identityservice.entity.User;
import com.identityservice.entity.entityEnum.StatusBasic;
import com.identityservice.entity.entityEnum.UserRole;
import com.identityservice.event.TaxpayerEventPublisher;
import com.identityservice.event.TaxpayerRegisteredEvent;
import com.identityservice.service.AuditLogService;
import com.identityservice.service.TaxpayerRegistrationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
@Slf4j
public class TaxpayerRegistrationServiceImpl implements TaxpayerRegistrationService {

    private final UserRepository userRepository;
    private final TaxpayerRepository taxpayerRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuditLogService auditLogService;
    private final TaxpayerEventPublisher eventPublisher;
    private final RestTemplate restTemplate;

    @Value("${taxpayer.service.url:http://localhost:8088}")
    private String taxpayerServiceUrl;

    @Override
    @Transactional
    public TaxpayerRegistrationResponseDto registerTaxpayer(TaxpayerRegistrationRequestDto request) {
        log.info("Processing registration for email: {}", request.getEmail());

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email already registered: " + request.getEmail());
        }

        // Normalize security answer: trim + lowercase before hashing
        // This makes the answer case-insensitive (e.g. "Paris" == "paris")
        String normalizedAnswer = request.getSecurityAnswer().trim().toLowerCase();

        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .securityAnswerHash(passwordEncoder.encode(normalizedAnswer))
                .role(UserRole.TAXPAYER)
                .status(StatusBasic.Active)
                .build();

        User savedUser = userRepository.save(user);

        Taxpayer taxpayer = Taxpayer.builder()
                .user(savedUser)
                .name(savedUser.getName())
                .type(request.getTaxpayerType())
                .address(request.getAddress())
                .contactInfo(request.getContactInfo())
                .build();
        taxpayerRepository.save(taxpayer);

        auditLogService.recordRegistration(savedUser, "TAXPAYER_REGISTER", "auth/register");

        TaxpayerRegisteredEvent event = TaxpayerRegisteredEvent.builder()
                .userId(savedUser.getId())
                .name(savedUser.getName())
                .email(savedUser.getEmail())
                .phone(savedUser.getPhone())
                .taxpayerType(request.getTaxpayerType().name())
                .address(request.getAddress())
                .contactInfo(request.getContactInfo())
                .build();

        // Direct REST call to taxpayer-service (reliable)
        try {
            restTemplate.postForObject(
                    taxpayerServiceUrl + "/api/taxpayers/internal/create",
                    event, Object.class);
            log.info("Taxpayer profile created via REST for userId: {}", savedUser.getId());
        } catch (Exception e) {
            log.warn("REST call to taxpayer-service failed, falling back to Kafka: {}", e.getMessage());
            try { eventPublisher.publishTaxpayerRegistered(event); } catch (Exception ke) {
                log.error("Kafka publish also failed: {}", ke.getMessage());
            }
        }

        log.info("Registration successful for email: {}", request.getEmail());
        return TaxpayerRegistrationResponseDto.builder()
                .taxpayerIdNumber(savedUser.getId())
                .name(savedUser.getName())
                .email(savedUser.getEmail())
                .build();
    }
}
