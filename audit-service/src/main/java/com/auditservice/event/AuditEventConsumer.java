package com.auditservice.event;

import com.auditservice.dto.ComplianceRequest;
import com.auditservice.entity.enums.ComplianceType;
import com.auditservice.service.AuditService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class AuditEventConsumer {

    private final AuditService auditService;

    @KafkaListener(topics = "filing.submitted", groupId = "audit-group", autoStartup = "${kafka.consumer.auto-startup:false}")
    public void onFilingSubmitted(FilingSubmittedEvent event) {
        log.info("Received filing.submitted event — creating compliance record for filingId: {}", event.getFilingId());
        ComplianceRequest request = new ComplianceRequest();
        request.setEntityId(event.getFilingId());
        request.setType(ComplianceType.FILING);
        request.setResult("Pending Review");
        request.setNotes("Auto-created on filing submission for period: " + event.getPeriod());
        auditService.createComplianceRecord(request);
    }
}
