package com.notificationservice.seeder;

import com.notificationservice.entity.Notification;
import com.notificationservice.entity.enums.NotificationCategory;
import com.notificationservice.entity.enums.NotificationStatus;
import com.notificationservice.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class NotificationDataSeeder implements CommandLineRunner {

    private final NotificationRepository notificationRepository;

    @Override
    @Transactional
    public void run(String... args) {
        if (notificationRepository.count() > 0) {
            log.info("Notification data already exists. Skipping seeder.");
            return;
        }

        log.info("Seeding notifications...");

        // userId mapping (after 9 internal users):
        // Rahul Sharma -> userId=10
        // Priya Nair   -> userId=11
        // Arjun Mehta  -> userId=12
        // Sneha Reddy  -> userId=13

        // --- BROADCAST (userId=0, visible to ALL users) ---
        save(0L, null, "TaxEase System Maintenance scheduled on 15th Jan 2025 from 2AM to 4AM IST. Please plan accordingly.",
                NotificationCategory.BROADCAST, NotificationStatus.UNREAD);
        save(0L, null, "New tax filing portal features are now live! Check the dashboard for updates.",
                NotificationCategory.PROGRAM_UPDATE, NotificationStatus.UNREAD);
        save(0L, null, "Important: Tax filing deadline for FY 2024-25 is 31st July 2025. File early to avoid penalties.",
                NotificationCategory.DEADLINE_ALERT, NotificationStatus.UNREAD);

        // --- RAHUL SHARMA (userId=10) ---
        save(10L, 1L, "Your tax filing #1 for period 2022-23 has been submitted successfully.",
                NotificationCategory.FILING, NotificationStatus.READ);
        save(10L, 1L, "Your tax filing #1 for period 2022-23 has been APPROVED by the officer.",
                NotificationCategory.FILING, NotificationStatus.READ);
        save(10L, 1L, "Payment of ₹48,500 for filing #1 (2022-23) has been completed successfully.",
                NotificationCategory.PAYMENT, NotificationStatus.READ);
        save(10L, 2L, "Your tax filing #2 for period 2023-24 has been submitted and is under review.",
                NotificationCategory.FILING, NotificationStatus.UNREAD);
        save(10L, null, "Reminder: Your tax filing for period 2024-25 is due on 31st July 2025. Please submit before the deadline.",
                NotificationCategory.DEADLINE_ALERT, NotificationStatus.UNREAD);
        save(10L, null, "Compliance check completed for your account. No issues found for FY 2022-23.",
                NotificationCategory.COMPLIANCE, NotificationStatus.UNREAD);

        // --- PRIYA NAIR (userId=11) ---
        save(11L, 4L, "Your tax filing #4 for period 2022-23 has been submitted successfully.",
                NotificationCategory.FILING, NotificationStatus.READ);
        save(11L, 4L, "Your tax filing #4 for period 2022-23 has been APPROVED by the officer.",
                NotificationCategory.FILING, NotificationStatus.READ);
        save(11L, 4L, "Payment of ₹32,000 for filing #4 (2022-23) has been completed successfully.",
                NotificationCategory.PAYMENT, NotificationStatus.READ);
        save(11L, 5L, "Your tax filing #5 for period 2023-24 has been submitted and is under review.",
                NotificationCategory.FILING, NotificationStatus.UNREAD);
        save(11L, null, "Reminder: Your tax filing for period 2024-25 is due on 31st July 2025.",
                NotificationCategory.DEADLINE_ALERT, NotificationStatus.UNREAD);

        // --- ARJUN MEHTA (userId=12) ---
        save(12L, 6L, "Your tax filing #6 for period 2022-23 has been submitted successfully.",
                NotificationCategory.FILING, NotificationStatus.READ);
        save(12L, 6L, "Your tax filing #6 for period 2022-23 has been APPROVED by the officer.",
                NotificationCategory.FILING, NotificationStatus.READ);
        save(12L, 6L, "Payment of ₹3,75,000 for filing #6 (2022-23) has been completed successfully.",
                NotificationCategory.PAYMENT, NotificationStatus.READ);
        save(12L, 7L, "Your tax filing #7 for period 2023-24 has been REJECTED. Please review and resubmit with correct documents.",
                NotificationCategory.FILING, NotificationStatus.UNREAD);
        save(12L, 8L, "Your revised tax filing #8 for period 2024-25 has been submitted and is under review.",
                NotificationCategory.FILING, NotificationStatus.UNREAD);
        save(12L, null, "Compliance alert: Please ensure GST returns are filed before the due date to avoid penalties.",
                NotificationCategory.COMPLIANCE, NotificationStatus.UNREAD);
        save(12L, null, "Reminder: Business tax filing for period 2024-25 is due on 31st October 2025.",
                NotificationCategory.DEADLINE_ALERT, NotificationStatus.UNREAD);

        // --- INTERNAL STAFF (userId=1..9) ---
        save(1L, null, "System report: All services are running normally. Monthly audit summary is ready.",
                NotificationCategory.SYSTEM_UPDATE, NotificationStatus.UNREAD);
        save(2L, null, "You have 3 pending tax filings awaiting review and approval.",
                NotificationCategory.FILING, NotificationStatus.UNREAD);
        save(3L, null, "You have 2 submitted filings pending your review.",
                NotificationCategory.FILING, NotificationStatus.UNREAD);
        save(4L, null, "Monthly revenue report for December 2024 is now available.",
                NotificationCategory.SYSTEM_UPDATE, NotificationStatus.UNREAD);
        save(5L, null, "Q3 financial summary has been generated. Please review.",
                NotificationCategory.SYSTEM_UPDATE, NotificationStatus.UNREAD);
        save(6L, null, "Compliance review for FY 2024-25 is due. 5 cases require attention.",
                NotificationCategory.COMPLIANCE, NotificationStatus.UNREAD);
        save(7L, null, "Non-compliance alert: 2 taxpayers have overdue filings.",
                NotificationCategory.COMPLIANCE, NotificationStatus.UNREAD);
        save(8L, null, "Audit case #3 has been escalated and requires your immediate review.",
                NotificationCategory.COMPLIANCE, NotificationStatus.UNREAD);
        save(9L, null, "Audit case #5 has been assigned to you for review.",
                NotificationCategory.COMPLIANCE, NotificationStatus.UNREAD);

        // --- SNEHA REDDY (userId=13) ---
        save(13L, 9L, "Your tax filing #9 for period 2023-24 has been submitted successfully.",
                NotificationCategory.FILING, NotificationStatus.READ);
        save(13L, 9L, "Your tax filing #9 for period 2023-24 has been APPROVED by the officer.",
                NotificationCategory.FILING, NotificationStatus.READ);
        save(13L, 9L, "Payment of ₹41,000 for filing #9 (2023-24) has been completed successfully.",
                NotificationCategory.PAYMENT, NotificationStatus.READ);
        save(13L, null, "Welcome to TaxEase! Your account is active. Start by filing your tax return for FY 2024-25.",
                NotificationCategory.SYSTEM_UPDATE, NotificationStatus.UNREAD);
        save(13L, null, "Reminder: Your tax filing for period 2024-25 is due on 31st July 2025.",
                NotificationCategory.DEADLINE_ALERT, NotificationStatus.UNREAD);

        log.info("Notification seeding completed: {} notifications saved.", notificationRepository.count());
    }

    private void save(Long userId, Long entityId, String message,
                      NotificationCategory category, NotificationStatus status) {
        notificationRepository.saveAndFlush(Notification.builder()
                .userId(userId)
                .entityId(entityId)
                .message(message)
                .category(category)
                .status(status)
                .build());
    }
}
