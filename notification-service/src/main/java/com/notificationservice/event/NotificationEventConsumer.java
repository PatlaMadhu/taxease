package com.notificationservice.event;

import com.notificationservice.entity.enums.NotificationCategory;
import com.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class NotificationEventConsumer {

    private final NotificationService notificationService;

    @KafkaListener(topics = "taxpayer.registered")
    public void onTaxpayerRegistered(TaxpayerRegisteredEvent event) {
        log.info("Received taxpayer.registered event for userId: {}", event.getUserId());
        notificationService.sendNotificationToUser(
                event.getUserId(),
                "Welcome to TaxEase, " + event.getName() + "! Your account has been created successfully.",
                NotificationCategory.SYSTEM_UPDATE
        );
    }

    @KafkaListener(topics = "filing.submitted")
    public void onFilingSubmitted(FilingSubmittedEvent event) {
        log.info("Received filing.submitted event for filingId: {}", event.getFilingId());
        Long targetUserId = event.getUserId() != null ? event.getUserId() : event.getTaxpayerId();
        notificationService.sendNotificationToUser(
                targetUserId,
                "Your tax filing #" + event.getFilingId() + " for period " + event.getPeriod() + " has been submitted successfully.",
                NotificationCategory.FILING
        );
    }

    @KafkaListener(topics = "payment.completed")
    public void onPaymentCompleted(PaymentCompletedEvent event) {
        log.info("Received payment.completed event for paymentId: {}", event.getPaymentId());
        notificationService.sendNotificationToUser(
                event.getTaxpayerId(),
                "Payment of " + event.getAmount() + " for filing #" + event.getFilingId() + " has been completed successfully.",
                NotificationCategory.PAYMENT
        );
    }

    @KafkaListener(topics = "filing.deadline")
    public void onFilingDeadline(FilingDeadlineEvent event) {
        log.info("Received filing.deadline event for userId: {}", event.getUserId());
        notificationService.sendNotificationToUser(
                event.getUserId(),
                "Reminder: Your tax filing for period " + event.getPeriod() + " is due on " + event.getDeadlineDate() + ". Please submit before the deadline.",
                NotificationCategory.DEADLINE_ALERT
        );
    }

    @KafkaListener(topics = "compliance.updated")
    public void onComplianceUpdated(ComplianceUpdateEvent event) {
        log.info("Received compliance.updated event for userId: {}", event.getUserId());
        notificationService.sendNotificationToUser(
                event.getUserId(),
                "Compliance update for your account: " + event.getUpdateDetails(),
                NotificationCategory.COMPLIANCE
        );
    }

    @KafkaListener(topics = "program.update")
    public void onProgramUpdate(ProgramUpdateEvent event) {
        log.info("Received program.update event: {}", event.getTitle());
        notificationService.broadcastNotification(
                event.getTitle() + ": " + event.getDetails(),
                NotificationCategory.PROGRAM_UPDATE
        );
    }
}