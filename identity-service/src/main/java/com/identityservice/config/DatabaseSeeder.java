package com.identityservice.config;

import com.identityservice.dao.UserRepository;
import com.identityservice.entity.User;
import com.identityservice.entity.entityEnum.StatusBasic;
import com.identityservice.entity.entityEnum.UserRole;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class DatabaseSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) {
        if (!userRepository.existsByEmail("admin1@taxease.gov")) {
            log.info("Starting Database Seeding for Internal Roles...");
            try {
                String commonPassword = passwordEncoder.encode("Password123");
                // Default security answer for all seeded accounts: "chennai" (lowercase)
                // Question: "What is your favorite place?"
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

                log.info("Database Seeding Completed Successfully! (Default security answer: 'chennai')");
            } catch (Exception e) {
                log.error("Seeding failed: {}", e.getMessage());
                throw e;
            }
        } else {
            log.info("Database already contains users. Skipping seeder.");
        }
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
        log.debug("Created {} with role {}", email, role);
    }
}
