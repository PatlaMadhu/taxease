package com.reportservice.service.impl;

import com.reportservice.client.AuditClient;
import com.reportservice.client.PaymentClient;
import com.reportservice.dto.AuditDashboardResponse;
import com.reportservice.dto.PaymentMetricsResponse;
import com.reportservice.dto.ReportRequest;
import com.reportservice.dto.ReportResponse;
import com.reportservice.dto.RevenueDashboardResponse;
import com.reportservice.entity.Report;
import com.reportservice.entity.enums.ReportScope;
import com.reportservice.repository.ReportRepository;
import com.reportservice.service.ReportService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReportServiceImpl implements ReportService {

    private final ReportRepository reportRepository;
    private final PaymentClient paymentClient;
    private final AuditClient auditClient;

    @Override
    @Transactional
    public ReportResponse generateReport(ReportRequest request) {
        log.info("Generating {} report: {}", request.getScope(), request.getTitle());
        Report report = Report.builder()
                .scope(request.getScope())
                .title(request.getTitle())
                .metrics(request.getMetrics())
                .generatedBy(request.getGeneratedBy())
                .build();
        return toResponse(reportRepository.save(report));
    }

    @Override
    public ReportResponse getReportById(Long reportId) {
        return toResponse(reportRepository.findById(reportId)
                .orElseThrow(() -> new NoSuchElementException("Report not found: " + reportId)));
    }

    @Override
    public List<ReportResponse> getAllReports() {
        return reportRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Override
    public List<ReportResponse> getReportsByScope(ReportScope scope) {
        return reportRepository.findByScope(scope).stream().map(this::toResponse).toList();
    }

    @Override
    public PaymentMetricsResponse getPaymentMetrics() {
        log.info("Fetching payment metrics via Feign from payment-service");
        return paymentClient.getPaymentMetrics();
    }

    @Override
    public AuditDashboardResponse getAuditDashboard() {
        log.info("Fetching audit dashboard via Feign from audit-service");
        return auditClient.getAuditDashboard();
    }

    @Override
    public RevenueDashboardResponse getRevenueDashboard(String period, String taxpayerType) {
        log.info("Fetching revenue dashboard via Feign from payment-service");
        return paymentClient.getRevenueDashboard();
    }

    @Override
    public byte[] generateCustomReport(LocalDate startDate, LocalDate endDate,
                                       String reportType, List<String> metrics) {
        log.info("Generating custom report [{}] from {} to {}", reportType, startDate, endDate);
        StringBuilder csv = new StringBuilder();
        csv.append("TaxEase Custom Report\n");
        csv.append("Report Type:,").append(reportType).append("\n");
        csv.append("Date Range:,").append(startDate).append(",to,").append(endDate).append("\n\n");

        for (String metric : metrics) {
            csv.append("--- ").append(metric.toUpperCase(Locale.ROOT)).append(" DATA ---\n");
            List<Report> reports = reportRepository.findByScope(ReportScope.valueOf(metric.toUpperCase(Locale.ROOT)));
            if (reports.isEmpty()) {
                csv.append("No records found for this period.\n\n");
            } else {
                csv.append("Report ID,Scope,Title,Generated Date\n");
                for (Report r : reports) {
                    csv.append(r.getId()).append(",")
                            .append(r.getScope()).append(",")
                            .append(r.getTitle()).append(",")
                            .append(r.getGeneratedDate()).append("\n");
                }
                csv.append("\n");
            }
        }
        return csv.toString().getBytes(StandardCharsets.UTF_8);
    }

    private ReportResponse toResponse(Report r) {
        return ReportResponse.builder()
                .reportId(r.getId())
                .scope(r.getScope())
                .title(r.getTitle())
                .metrics(r.getMetrics())
                .generatedBy(r.getGeneratedBy())
                .generatedDate(r.getGeneratedDate())
                .build();
    }
}
