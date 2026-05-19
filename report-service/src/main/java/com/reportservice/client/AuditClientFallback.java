package com.reportservice.client;

import com.reportservice.dto.AuditDashboardResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AuditClientFallback implements AuditClient {

    @Override
    public AuditDashboardResponse getAuditDashboard() {
        log.warn("audit-service unavailable, returning fallback dashboard");
        return AuditDashboardResponse.builder()
                .totalAudits(0L)
                .openAudits(0L)
                .closedAudits(0L)
                .nonComplianceFilings(0L)
                .build();
    }
}
