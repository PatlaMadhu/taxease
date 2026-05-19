package com.taxfilingservice.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class FilingEventPublisher {

    private static final String TOPIC = "filing.submitted";
    private final KafkaTemplate<String, FilingSubmittedEvent> kafkaTemplate;

    @Async
    public void publishFilingSubmitted(FilingSubmittedEvent event) {
        log.info("Publishing filing.submitted event for filingId: {}", event.getFilingId());
        try {
            kafkaTemplate.send(TOPIC, String.valueOf(event.getFilingId()), event);
        } catch (Exception e) {
            log.warn("Failed to publish filing.submitted event: {}", e.getMessage());
        }
    }
}
