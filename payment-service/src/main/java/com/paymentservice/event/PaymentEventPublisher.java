package com.paymentservice.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class PaymentEventPublisher {

    private static final String TOPIC = "payment.completed";
    private final KafkaTemplate<String, PaymentCompletedEvent> kafkaTemplate;

    public void publishPaymentCompleted(PaymentCompletedEvent event) {
        log.info("Publishing payment.completed event for paymentId: {}", event.getPaymentId());
        try {
            kafkaTemplate.send(TOPIC, String.valueOf(event.getPaymentId()), event);
        } catch (Exception e) {
            log.warn("Failed to publish payment.completed event: {}", e.getMessage());
        }
    }
}
