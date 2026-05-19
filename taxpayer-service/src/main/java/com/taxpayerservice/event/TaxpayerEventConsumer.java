package com.taxpayerservice.event;

import com.taxpayerservice.entity.Taxpayer;
import com.taxpayerservice.entity.enums.TaxpayerType;
import com.taxpayerservice.repository.TaxpayerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
@RequiredArgsConstructor
@Slf4j
public class TaxpayerEventConsumer {

    private final TaxpayerRepository taxpayerRepository;

    @KafkaListener(topics = "taxpayer.registered", groupId = "taxpayer-group")
    public void onTaxpayerRegistered(TaxpayerRegisteredEvent event) {
        log.info("Received taxpayer.registered event for userId: {}", event.getUserId());

        if (taxpayerRepository.existsByEmail(event.getEmail())) {
            log.warn("Taxpayer with email {} already exists, skipping creation", event.getEmail());
            return;
        }

        TaxpayerType type = Arrays.stream(TaxpayerType.values())
                .filter(t -> t.name().equalsIgnoreCase(event.getTaxpayerType()))
                .findFirst()
                .orElse(TaxpayerType.Citizen);

        Taxpayer taxpayer = Taxpayer.builder()
                .userId(event.getUserId())
                .name(event.getName())
                .email(event.getEmail())
                .phone(event.getPhone())
                .type(type)
                .address(event.getAddress())
                .contactInfo(event.getContactInfo())
                .build();

        taxpayerRepository.save(taxpayer);
        log.info("Taxpayer profile created for userId: {}", event.getUserId());
    }
}
