package com.identityservice.config;

import com.identityservice.dao.TaxpayerRepository;
import com.identityservice.dao.UserRepository;
import com.identityservice.entity.Taxpayer;
import com.identityservice.entity.User;
import com.identityservice.entity.entityEnum.StatusBasic;
import com.identityservice.entity.entityEnum.TaxpayerType;
import com.identityservice.entity.entityEnum.UserRole;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class DatabaseSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final TaxpayerRepository taxpayerRepository;
    private final PasswordEncoder passwordEncoder;
    private final RestTemplate restTemplate;

    private static final String TAXPAYER_SERVICE_URL = "http://localhost:8088/api/taxpayers/internal/create";

    @Override
    @Transactional
    public void run(String... args) {
        if (!userRepository.existsByEmail("admin1@taxease.gov")) {
            log.info("Starting Database Seeding for Internal Roles...");
            try {
                String commonPassword = passwordEncoder.encode("Password123");
                String defaultSecurityAnswer = passwordEncoder.encode("chennai");

                createUser("Admin",              "admin1@taxease.gov",       UserRole.ADMINISTRATOR, commonPassword, defaultSecurityAnswer);
                createUser("Officer Sarah",      "officer1@taxease.gov",     UserRole.OFFICER,       commonPassword, defaultSecurityAnswer);
                createUser("Officer James",      "officer2@taxease.gov",     UserRole.OFFICER,       commonPassword, defaultSecurityAnswer);
                createUser("Manager Mike",       "manager1@taxease.gov",     UserRole.MANAGER,       commonPassword, defaultSecurityAnswer);
                createUser("Manager Elena",      "manager2@taxease.gov",     UserRole.MANAGER,       commonPassword, defaultSecurityAnswer);
                createUser("Compliance Lead",    "compliance1@taxease.gov",  UserRole.COMPLIANCE,    commonPassword, defaultSecurityAnswer);
                createUser("Compliance Officer", "compliance2@taxease.gov",  UserRole.COMPLIANCE,    commonPassword, defaultSecurityAnswer);
                createUser("Auditor David",      "auditor1@taxease.gov",     UserRole.AUDITOR,       commonPassword, defaultSecurityAnswer);
                createUser("Auditor Sophia",     "auditor2@taxease.gov",     UserRole.AUDITOR,       commonPassword, defaultSecurityAnswer);

                log.info("Internal roles seeded. Seeding taxpayer accounts...");
            } catch (Exception e) {
                log.error("Internal role seeding failed: {}", e.getMessage());
                throw e;
            }
        } else {
            log.info("Internal role accounts already exist. Skipping internal seeding.");
        }

        // Seed taxpayer accounts independently
        seedTaxpayer("Rahul Sharma",   "rahul.sharma@gmail.com",   "9876543210", TaxpayerType.Citizen,  "12 MG Road, Bengaluru, Karnataka 560001",    "rahul.sharma@gmail.com");
        seedTaxpayer("Priya Nair",     "priya.nair@gmail.com",     "9845012345", TaxpayerType.Citizen,  "45 Anna Salai, Chennai, Tamil Nadu 600002",  "priya.nair@gmail.com");
        seedTaxpayer("Arjun Mehta",    "arjun.mehta@gmail.com",    "9123456780", TaxpayerType.Business, "78 Connaught Place, New Delhi 110001",        "arjun.mehta@gmail.com");
        seedTaxpayer("Sneha Reddy",    "sneha.reddy@gmail.com",    "9988776655", TaxpayerType.Citizen,  "23 Banjara Hills, Hyderabad, Telangana 500034","sneha.reddy@gmail.com");

        log.info("Database Seeding Completed. (Password: Password123 | Security answer: chennai)");
    }

    private void createUser(String name, String email, UserRole role, String passwordHash, String securityAnswerHash) {
        User user = User.builder()
                .name(name)
                .email(email)
                .phone("9998887770")
                .passwordHash(passwordHash)
                .securityAnswerHash(securityAnswerHash)
                .role(role)
                .status(StatusBasic.Active)
                .build();
        userRepository.saveAndFlush(user);
        log.info("Created user: {} | email: {} | role: {}", name, email, role);
    }

    private void seedTaxpayer(String name, String email, String phone,
                               TaxpayerType type, String address, String contactInfo) {
        if (userRepository.existsByEmail(email)) {
            log.debug("Taxpayer {} already exists, skipping.", email);
            return;
        }

        String passwordHash       = passwordEncoder.encode("Password123");
        String securityAnswerHash = passwordEncoder.encode("chennai");

        // 1. Save User
        User user = User.builder()
                .name(name)
                .email(email)
                .phone(phone)
                .passwordHash(passwordHash)
                .securityAnswerHash(securityAnswerHash)
                .role(UserRole.TAXPAYER)
                .status(StatusBasic.Active)
                .build();
        User savedUser = userRepository.saveAndFlush(user);

        // 2. Save Taxpayer in identity DB
        Taxpayer taxpayer = Taxpayer.builder()
                .user(savedUser)
                .name(name)
                .type(type)
                .address(address)
                .contactInfo(contactInfo)
                .build();
        taxpayerRepository.saveAndFlush(taxpayer);

        // 3. Notify taxpayer-service to create profile
        try {
            Map<String, Object> event = Map.of(
                "userId",       savedUser.getId(),
                "name",         name,
                "email",        email,
                "phone",        phone,
                "taxpayerType", type.name(),
                "address",      address,
                "contactInfo",  contactInfo
            );
            restTemplate.postForObject(TAXPAYER_SERVICE_URL, event, Object.class);
            log.info("Taxpayer profile created in taxpayer-service for: {}", email);
        } catch (Exception e) {
            log.warn("Could not notify taxpayer-service for {} (profile may need manual creation): {}", email, e.getMessage());
        }

        log.info("Seeded taxpayer: {} ({})", name, email);
    }
}
