package com.reportservice.client;

import com.reportservice.dto.AuditDashboardResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "AUDIT-SERVICE", fallback = AuditClientFallback.class)
public interface AuditClient {

    @GetMapping("/api/audits/dashboard")
    AuditDashboardResponse getAuditDashboard();
}
