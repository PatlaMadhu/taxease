package com.reportservice.config;

import com.reportservice.entity.Report;
import com.reportservice.entity.enums.ReportScope;
import com.reportservice.repository.ReportRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class ReportDataSeeder implements CommandLineRunner {

    private final ReportRepository reportRepository;

    @Override
    @Transactional
    public void run(String... args) {
        if (reportRepository.count() > 0) {
            log.info("Report data already exists. Skipping seeder.");
            return;
        }

        log.info("Seeding reports...");

        // Internal user IDs:
        // admin1@taxease.gov   -> userId=1  (Admin)
        // manager1@taxease.gov -> userId=4  (Manager Mike)
        // manager2@taxease.gov -> userId=5  (Manager Elena)

        // ── FILING REPORTS ───────────────────────────────────────────────────
        saveReport(ReportScope.FILING, "Tax Filing Summary Report - FY 2022-23", 4L,
                "{\"totalFilings\":3,\"approved\":3,\"submitted\":0,\"rejected\":0,\"draft\":0," +
                "\"totalAmountDeclared\":2055000.00,\"period\":\"2022-23\"}");

        saveReport(ReportScope.FILING, "Tax Filing Summary Report - FY 2023-24", 4L,
                "{\"totalFilings\":4,\"approved\":1,\"submitted\":3,\"rejected\":1,\"draft\":0," +
                "\"totalAmountDeclared\":2785000.00,\"period\":\"2023-24\"}");

        saveReport(ReportScope.FILING, "Tax Filing Summary Report - FY 2024-25", 5L,
                "{\"totalFilings\":3,\"approved\":0,\"submitted\":1,\"rejected\":0,\"draft\":2," +
                "\"totalAmountDeclared\":2605000.00,\"period\":\"2024-25\"}");

        saveReport(ReportScope.FILING, "Annual Filing Overview Report - All Periods", 1L,
                "{\"totalFilings\":10,\"approved\":4,\"submitted\":3,\"rejected\":1,\"draft\":2," +
                "\"taxpayers\":[\"Rahul Sharma\",\"Priya Nair\",\"Arjun Mehta\",\"Sneha Reddy\"]," +
                "\"totalAmountDeclared\":7445000.00}");

        // ── PAYMENT REPORTS ──────────────────────────────────────────────────
        saveReport(ReportScope.PAYMENT, "Payment Collection Report - FY 2022-23", 4L,
                "{\"totalPayments\":3,\"completed\":3,\"pending\":0,\"failed\":0," +
                "\"totalCollected\":455500.00,\"period\":\"2022-23\"}");

        saveReport(ReportScope.PAYMENT, "Payment Collection Report - FY 2023-24", 5L,
                "{\"totalPayments\":2,\"completed\":1,\"pending\":1,\"failed\":0," +
                "\"totalCollected\":41000.00,\"pendingAmount\":52000.00,\"period\":\"2023-24\"}");

        saveReport(ReportScope.PAYMENT, "Revenue Dashboard Report - All Periods", 1L,
                "{\"totalRevenue\":496500.00,\"completedPayments\":4,\"pendingPayments\":1," +
                "\"paymentMethods\":{\"NetBanking\":1,\"UPI_GPAY\":1,\"Bank\":1,\"UPI_PHONEPE\":1,\"UPI\":1}," +
                "\"topTaxpayer\":\"Arjun Mehta\",\"topAmount\":375000.00}");

        // ── COMPLIANCE REPORTS ───────────────────────────────────────────────
        saveReport(ReportScope.COMPLIANCE, "Compliance Status Report - FY 2022-23", 4L,
                "{\"totalRecords\":3,\"compliant\":3,\"nonCompliant\":0,\"pendingReview\":0," +
                "\"period\":\"2022-23\",\"complianceRate\":\"100%\"}");

        saveReport(ReportScope.COMPLIANCE, "Compliance Status Report - FY 2023-24", 5L,
                "{\"totalRecords\":5,\"compliant\":1,\"nonCompliant\":1,\"pendingReview\":3," +
                "\"period\":\"2023-24\",\"complianceRate\":\"20%\",\"issues\":[\"Arjun Mehta - Income mismatch\"]}");

        saveReport(ReportScope.COMPLIANCE, "Annual Compliance Overview Report", 1L,
                "{\"totalRecords\":13,\"compliant\":6,\"nonCompliant\":1,\"pendingReview\":4,\"pending\":2," +
                "\"overallComplianceRate\":\"46%\",\"auditCases\":5,\"closedAudits\":2,\"openAudits\":2,\"inProgressAudits\":1}");

        // ── PROGRAM REPORTS ──────────────────────────────────────────────────
        saveReport(ReportScope.PROGRAM, "Taxpayer Enrollment Report - Q1 2025", 4L,
                "{\"totalTaxpayers\":4,\"citizens\":3,\"businesses\":1," +
                "\"newRegistrations\":4,\"activeAccounts\":4,\"period\":\"Q1-2025\"}");

        saveReport(ReportScope.PROGRAM, "System Usage Report - January 2025", 1L,
                "{\"totalLogins\":47,\"filingSubmissions\":10,\"paymentsProcessed\":5," +
                "\"notificationsSent\":28,\"reportsGenerated\":10,\"period\":\"Jan-2025\"}");

        log.info("Report seeding completed: {} reports saved.", reportRepository.count());
    }

    private void saveReport(ReportScope scope, String title, Long generatedBy, String metrics) {
        reportRepository.saveAndFlush(Report.builder()
                .scope(scope)
                .title(title)
                .generatedBy(generatedBy)
                .metrics(metrics)
                .build());
    }
}
