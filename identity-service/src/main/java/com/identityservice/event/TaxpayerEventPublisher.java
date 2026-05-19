package com.identityservice.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class TaxpayerEventPublisher {

    private static final String TOPIC = "taxpayer.registered";
    private final KafkaTemplate<String, TaxpayerRegisteredEvent> kafkaTemplate;

    public void publishTaxpayerRegistered(TaxpayerRegisteredEvent event) {
        log.info("Publishing taxpayer.registered event for userId: {}", event.getUserId());
        kafkaTemplate.send(TOPIC, String.valueOf(event.getUserId()), event);
    }
}
