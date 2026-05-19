package com.reportservice.controller;

import com.reportservice.dto.AuditDashboardResponse;
import com.reportservice.dto.PaymentMetricsResponse;
import com.reportservice.dto.ReportRequest;
import com.reportservice.dto.ReportResponse;
import com.reportservice.dto.RevenueDashboardResponse;
import com.reportservice.entity.enums.ReportScope;
import com.reportservice.service.ReportService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
@Validated
@Slf4j
public class ReportController {

    private final ReportService reportService;

    @PostMapping
    public ResponseEntity<ReportResponse> generateReport(@Valid @RequestBody ReportRequest request) {
        log.info("Generating report: {}", request.getTitle());
        return ResponseEntity.status(HttpStatus.CREATED).body(reportService.generateReport(request));
    }

    @GetMapping
    public ResponseEntity<List<ReportResponse>> getAllReports() {
        return ResponseEntity.ok(reportService.getAllReports());
    }

    @GetMapping("/{reportId}")
    public ResponseEntity<ReportResponse> getReportById(@PathVariable Long reportId) {
        return ResponseEntity.ok(reportService.getReportById(reportId));
    }

    @GetMapping("/scope/{scope}")
    public ResponseEntity<List<ReportResponse>> getByScope(@PathVariable ReportScope scope) {
        return ResponseEntity.ok(reportService.getReportsByScope(scope));
    }

    @GetMapping("/payments/metrics")
    public ResponseEntity<PaymentMetricsResponse> getPaymentMetrics() {
        log.info("Fetching payment metrics");
        return ResponseEntity.ok(reportService.getPaymentMetrics());
    }

    @GetMapping("/audits/dashboard")
    public ResponseEntity<AuditDashboardResponse> getAuditDashboard() {
        log.info("Fetching audit dashboard");
        return ResponseEntity.ok(reportService.getAuditDashboard());
    }

    @GetMapping("/revenue/dashboard")
    public ResponseEntity<RevenueDashboardResponse> getRevenueDashboard(
            @RequestParam(required = false) String period,
            @RequestParam(required = false) String taxpayerType) {
        log.info("Fetching revenue dashboard for period: {} type: {}", period, taxpayerType);
        return ResponseEntity.ok(reportService.getRevenueDashboard(period, taxpayerType));
    }

    @GetMapping("/custom/download")
    public ResponseEntity<byte[]> downloadCustomReport(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam String reportType,
            @Valid @NotEmpty(message = "At least one metric must be selected")
            @RequestParam List<String> metrics) {
        log.info("Generating custom report [{}] from {} to {}", reportType, startDate, endDate);
        byte[] data = reportService.generateCustomReport(startDate, endDate, reportType, metrics);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"report.csv\"")
                .contentType(MediaType.parseMediaType("text/csv"))
                .body(data);
    }
}
