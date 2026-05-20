package com.auditservice.config;

import com.auditservice.entity.AuditCase;
import com.auditservice.entity.ComplianceRecord;
import com.auditservice.entity.enums.AuditStatus;
import com.auditservice.entity.enums.ComplianceType;
import com.auditservice.repository.AuditCaseRepository;
import com.auditservice.repository.ComplianceRecordRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class AuditDataSeeder implements CommandLineRunner {

    private final AuditCaseRepository auditCaseRepository;
    private final ComplianceRecordRepository complianceRecordRepository;
    private final RestTemplate restTemplate;

    private static final String FILING_URL    = "http://localhost:8083/api/filings/taxpayer/";
    private static final String IDENTITY_URL  = "http://localhost:8082/api/auth/users/by-email/";

    @Override
    @Transactional
    public void run(String... args) {
        if (auditCaseRepository.count() > 0) {
            log.info("Audit data already exists. Skipping seeder.");
            return;
        }

        log.info("Seeding audit cases and compliance records...");

        Long officer1Id  = fetchUserId("officer1@taxease.gov");
        Long officer2Id  = fetchUserId("officer2@taxease.gov");
        Long auditor1Id  = fetchUserId("auditor1@taxease.gov");
        Long auditor2Id  = fetchUserId("auditor2@taxease.gov");

        // taxpayerId is the auto-generated PK in taxpayer-service (1,2,3,4 in order)
        // We fetch filings to get actual filing IDs
        List<Map<String,Object>> rahulFilings = fetchFilings(1L);
        List<Map<String,Object>> priyaFilings = fetchFilings(2L);
        List<Map<String,Object>> arjunFilings = fetchFilings(3L);
        List<Map<String,Object>> snehaFilings = fetchFilings(4L);

        // ── AUDIT CASES ──────────────────────────────────────────────────────
        if (officer1Id != null)
            saveAudit(officer1Id, 3L,
                    "Full audit of business tax filings for FY 2023-24 due to discrepancies in declared income.",
                    "Income declared does not match GST returns. Penalty of Rs.25,000 recommended.",
                    AuditStatus.CLOSED);

        if (officer2Id != null)
            saveAudit(officer2Id, 1L,
                    "Routine compliance audit for FY 2022-23.",
                    "All documents verified. No discrepancies found.",
                    AuditStatus.CLOSED);

        if (auditor1Id != null)
            saveAudit(auditor1Id, 3L,
                    "Escalated audit for FY 2024-25 business filing — GST mismatch investigation.",
                    null, AuditStatus.IN_PROGRESS);

        if (auditor2Id != null)
            saveAudit(auditor2Id, 2L,
                    "Verification of supporting documents for FY 2023-24 filing.",
                    null, AuditStatus.OPEN);

        if (officer1Id != null)
            saveAudit(officer1Id, 4L,
                    "Spot check audit for FY 2024-25 draft filing.",
                    null, AuditStatus.OPEN);

        // ── COMPLIANCE RECORDS ───────────────────────────────────────────────
        // Use actual filing IDs fetched from filing-service
        addFilingCompliance(rahulFilings, "2022-23", "Compliant",     "Filing verified for FY 2022-23. All documents in order.");
        addFilingCompliance(rahulFilings, "2023-24", "Pending Review","Filing for FY 2023-24 submitted and under officer review.");
        addFilingCompliance(priyaFilings, "2022-23", "Compliant",     "Filing verified for FY 2022-23. All documents in order.");
        addFilingCompliance(priyaFilings, "2023-24", "Pending Review","Filing for FY 2023-24 submitted and under officer review.");
        addFilingCompliance(arjunFilings, "2022-23", "Compliant",     "Business filing verified for FY 2022-23.");
        addFilingCompliance(arjunFilings, "2023-24", "Non-Compliant", "Income mismatch detected in FY 2023-24 business filing. Rejected.");
        addFilingCompliance(arjunFilings, "2024-25", "Pending Review","Revised business filing for FY 2024-25 under review.");
        addFilingCompliance(snehaFilings, "2023-24", "Compliant",     "Filing verified for FY 2023-24. All documents in order.");

        addPaymentCompliance(rahulFilings, "2022-23", "Compliant", "Tax payment of Rs.48,500 received for FY 2022-23.");
        addPaymentCompliance(priyaFilings, "2022-23", "Compliant", "Tax payment of Rs.32,000 received for FY 2022-23.");
        addPaymentCompliance(arjunFilings, "2022-23", "Compliant", "Tax payment of Rs.3,75,000 received for FY 2022-23.");
        addPaymentCompliance(snehaFilings, "2023-24", "Compliant", "Tax payment of Rs.41,000 received for FY 2023-24.");
        addPaymentCompliance(rahulFilings, "2023-24", "Pending",   "Payment of Rs.52,000 pending for FY 2023-24.");

        log.info("Audit seeding completed: {} audit cases, {} compliance records.",
                auditCaseRepository.count(), complianceRecordRepository.count());
    }

    private void addFilingCompliance(List<Map<String,Object>> filings, String period, String result, String notes) {
        if (filings == null) return;
        filings.stream()
                .filter(f -> period.equals(f.get("period")))
                .findFirst()
                .ifPresent(f -> saveCompliance(((Number) f.get("filingId")).longValue(), ComplianceType.FILING, result, notes));
    }

    private void addPaymentCompliance(List<Map<String,Object>> filings, String period, String result, String notes) {
        if (filings == null) return;
        filings.stream()
                .filter(f -> period.equals(f.get("period")))
                .findFirst()
                .ifPresent(f -> saveCompliance(((Number) f.get("filingId")).longValue(), ComplianceType.PAYMENT, result, notes));
    }

    private List<Map<String,Object>> fetchFilings(Long taxpayerId) {
        try {
            return restTemplate.exchange(FILING_URL + taxpayerId, HttpMethod.GET, null,
                    new ParameterizedTypeReference<List<Map<String,Object>>>() {}).getBody();
        } catch (Exception e) {
            log.warn("Could not fetch filings for taxpayerId={}: {}", taxpayerId, e.getMessage());
            return null;
        }
    }

    private Long fetchUserId(String email) {
        try {
            Map<?,?> res = restTemplate.getForObject(IDENTITY_URL + email, Map.class);
            return res != null ? ((Number) res.get("userId")).longValue() : null;
        } catch (Exception e) {
            log.warn("Could not fetch userId for {}: {}", email, e.getMessage());
            return null;
        }
    }

    private void saveAudit(Long officerId, Long taxpayerId, String scope, String findings, AuditStatus status) {
        auditCaseRepository.saveAndFlush(AuditCase.builder()
                .officerId(officerId).taxpayerId(taxpayerId)
                .scope(scope).findings(findings).status(status).build());
    }

    private void saveCompliance(Long entityId, ComplianceType type, String result, String notes) {
        complianceRecordRepository.saveAndFlush(ComplianceRecord.builder()
                .entityId(entityId).type(type).result(result).notes(notes).build());
    }
}
