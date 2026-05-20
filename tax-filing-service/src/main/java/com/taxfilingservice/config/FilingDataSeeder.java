package com.taxfilingservice.config;

import com.taxfilingservice.entity.FilingDocument;
import com.taxfilingservice.entity.TaxFiling;
import com.taxfilingservice.entity.enums.FilingDocType;
import com.taxfilingservice.entity.enums.FilingStatus;
import com.taxfilingservice.entity.enums.VerificationStatus;
import com.taxfilingservice.repository.FilingDocumentRepository;
import com.taxfilingservice.repository.TaxFilingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Map;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class FilingDataSeeder implements CommandLineRunner {

    private final TaxFilingRepository filingRepository;
    private final FilingDocumentRepository documentRepository;
    private final RestTemplate restTemplate;

    private static final String TAXPAYER_URL = "http://localhost:8088/api/taxpayers/by-email/";

    @Override
    @Transactional
    public void run(String... args) {
        if (filingRepository.count() > 0) {
            log.info("Filing data already exists. Skipping seeder.");
            return;
        }

        log.info("Seeding tax filings and documents...");

        Long[] rahul = fetchIds("rahul.sharma@gmail.com");
        Long[] priya = fetchIds("priya.nair@gmail.com");
        Long[] arjun = fetchIds("arjun.mehta@gmail.com");
        Long[] sneha = fetchIds("sneha.reddy@gmail.com");

        if (rahul == null || priya == null || arjun == null || sneha == null) {
            log.warn("Could not fetch taxpayer IDs. Filings will not be seeded. Ensure taxpayer-service is running.");
            return;
        }

        // rahul[0]=taxpayerId, rahul[1]=userId
        TaxFiling r1 = saveFiling(rahul[0], rahul[1], "rahul.sharma@gmail.com", "2022-23", new BigDecimal("485000.00"), FilingStatus.APPROVED);
        saveDoc(r1, FilingDocType.RETURN,         "https://storage.taxease.gov/docs/rahul/return_2022_23.pdf",        VerificationStatus.VERIFIED);
        saveDoc(r1, FilingDocType.SUPPORTING_DOC, "https://storage.taxease.gov/docs/rahul/form16_2022_23.pdf",        VerificationStatus.VERIFIED);

        TaxFiling r2 = saveFiling(rahul[0], rahul[1], "rahul.sharma@gmail.com", "2023-24", new BigDecimal("520000.00"), FilingStatus.SUBMITTED);
        saveDoc(r2, FilingDocType.RETURN,         "https://storage.taxease.gov/docs/rahul/return_2023_24.pdf",        VerificationStatus.PENDING);
        saveDoc(r2, FilingDocType.SUPPORTING_DOC, "https://storage.taxease.gov/docs/rahul/bank_statement_2023_24.pdf",VerificationStatus.PENDING);

        TaxFiling r3 = saveFiling(rahul[0], rahul[1], "rahul.sharma@gmail.com", "2024-25", new BigDecimal("560000.00"), FilingStatus.DRAFT);
        saveDoc(r3, FilingDocType.SUPPORTING_DOC, "https://storage.taxease.gov/docs/rahul/salary_slip_2024_25.pdf",   VerificationStatus.PENDING);

        TaxFiling p1 = saveFiling(priya[0], priya[1], "priya.nair@gmail.com", "2022-23", new BigDecimal("320000.00"), FilingStatus.APPROVED);
        saveDoc(p1, FilingDocType.RETURN,         "https://storage.taxease.gov/docs/priya/return_2022_23.pdf",        VerificationStatus.VERIFIED);
        saveDoc(p1, FilingDocType.SUPPORTING_DOC, "https://storage.taxease.gov/docs/priya/form16_2022_23.pdf",        VerificationStatus.VERIFIED);

        TaxFiling p2 = saveFiling(priya[0], priya[1], "priya.nair@gmail.com", "2023-24", new BigDecimal("375000.00"), FilingStatus.SUBMITTED);
        saveDoc(p2, FilingDocType.RETURN,         "https://storage.taxease.gov/docs/priya/return_2023_24.pdf",        VerificationStatus.PENDING);

        TaxFiling a1 = saveFiling(arjun[0], arjun[1], "arjun.mehta@gmail.com", "2022-23", new BigDecimal("1250000.00"), FilingStatus.APPROVED);
        saveDoc(a1, FilingDocType.RETURN,         "https://storage.taxease.gov/docs/arjun/return_2022_23.pdf",        VerificationStatus.VERIFIED);
        saveDoc(a1, FilingDocType.SUPPORTING_DOC, "https://storage.taxease.gov/docs/arjun/balance_sheet_2022_23.pdf", VerificationStatus.VERIFIED);
        saveDoc(a1, FilingDocType.SUPPORTING_DOC, "https://storage.taxease.gov/docs/arjun/gst_2022_23.pdf",           VerificationStatus.VERIFIED);

        TaxFiling a2 = saveFiling(arjun[0], arjun[1], "arjun.mehta@gmail.com", "2023-24", new BigDecimal("1480000.00"), FilingStatus.REJECTED);
        saveDoc(a2, FilingDocType.RETURN,         "https://storage.taxease.gov/docs/arjun/return_2023_24.pdf",        VerificationStatus.REJECTED);
        saveDoc(a2, FilingDocType.SUPPORTING_DOC, "https://storage.taxease.gov/docs/arjun/balance_sheet_2023_24.pdf", VerificationStatus.REJECTED);

        TaxFiling a3 = saveFiling(arjun[0], arjun[1], "arjun.mehta@gmail.com", "2024-25", new BigDecimal("1600000.00"), FilingStatus.SUBMITTED);
        saveDoc(a3, FilingDocType.RETURN,         "https://storage.taxease.gov/docs/arjun/return_2024_25.pdf",        VerificationStatus.PENDING);
        saveDoc(a3, FilingDocType.SUPPORTING_DOC, "https://storage.taxease.gov/docs/arjun/gst_2024_25.pdf",           VerificationStatus.PENDING);

        TaxFiling s1 = saveFiling(sneha[0], sneha[1], "sneha.reddy@gmail.com", "2023-24", new BigDecimal("410000.00"), FilingStatus.APPROVED);
        saveDoc(s1, FilingDocType.RETURN,         "https://storage.taxease.gov/docs/sneha/return_2023_24.pdf",        VerificationStatus.VERIFIED);
        saveDoc(s1, FilingDocType.SUPPORTING_DOC, "https://storage.taxease.gov/docs/sneha/form16_2023_24.pdf",        VerificationStatus.VERIFIED);

        TaxFiling s2 = saveFiling(sneha[0], sneha[1], "sneha.reddy@gmail.com", "2024-25", new BigDecimal("445000.00"), FilingStatus.DRAFT);
        saveDoc(s2, FilingDocType.SUPPORTING_DOC, "https://storage.taxease.gov/docs/sneha/salary_slip_2024_25.pdf",   VerificationStatus.PENDING);

        log.info("Filing seeding completed: {} filings.", filingRepository.count());
    }

    /** Returns [taxpayerId, userId] for the given email, or null if not found */
    private Long[] fetchIds(String email) {
        try {
            Map<?, ?> res = restTemplate.getForObject(TAXPAYER_URL + email, Map.class);
            if (res != null) {
                Long taxpayerId = ((Number) res.get("taxpayerId")).longValue();
                Long userId     = ((Number) res.get("userId")).longValue();
                return new Long[]{taxpayerId, userId};
            }
        } catch (Exception e) {
            log.warn("Could not fetch taxpayer for {}: {}", email, e.getMessage());
        }
        return null;
    }

    private TaxFiling saveFiling(Long taxpayerId, Long userId, String email,
                                  String period, BigDecimal amount, FilingStatus status) {
        return filingRepository.saveAndFlush(TaxFiling.builder()
                .taxpayerId(taxpayerId)
                .userId(userId)
                .taxpayerEmail(email)
                .period(period)
                .amountDeclared(amount)
                .status(status)
                .build());
    }

    private void saveDoc(TaxFiling filing, FilingDocType docType, String uri, VerificationStatus vs) {
        documentRepository.saveAndFlush(FilingDocument.builder()
                .taxFiling(filing)
                .docType(docType)
                .fileUri(uri)
                .verificationStatus(vs)
                .build());
    }
}
