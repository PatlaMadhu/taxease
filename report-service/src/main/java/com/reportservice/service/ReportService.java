package com.reportservice.service;

import com.reportservice.dto.AuditDashboardResponse;
import com.reportservice.dto.PaymentMetricsResponse;
import com.reportservice.dto.ReportRequest;
import com.reportservice.dto.ReportResponse;
import com.reportservice.dto.RevenueDashboardResponse;
import com.reportservice.entity.enums.ReportScope;

import java.time.LocalDate;
import java.util.List;

public interface ReportService {
    ReportResponse generateReport(ReportRequest request);
    ReportResponse getReportById(Long reportId);
    List<ReportResponse> getAllReports();
    List<ReportResponse> getReportsByScope(ReportScope scope);
    PaymentMetricsResponse getPaymentMetrics();
    AuditDashboardResponse getAuditDashboard();
    RevenueDashboardResponse getRevenueDashboard(String period, String taxpayerType);
    byte[] generateCustomReport(LocalDate startDate, LocalDate endDate, String reportType, List<String> metrics);
}
