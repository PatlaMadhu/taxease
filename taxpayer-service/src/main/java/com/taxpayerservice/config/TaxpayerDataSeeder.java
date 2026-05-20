package com.taxpayerservice.config;

import com.taxpayerservice.entity.Taxpayer;
import com.taxpayerservice.entity.TaxpayerDocument;
import com.taxpayerservice.entity.enums.DocType;
import com.taxpayerservice.entity.enums.TaxpayerType;
import com.taxpayerservice.entity.enums.VerificationStatus;
import com.taxpayerservice.repository.TaxpayerDocumentRepository;
import com.taxpayerservice.repository.TaxpayerRepository;
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
public class TaxpayerDataSeeder implements CommandLineRunner {

    private final TaxpayerRepository taxpayerRepository;
    private final TaxpayerDocumentRepository documentRepository;
    private final RestTemplate restTemplate;

    private static final String IDENTITY_URL = "http://localhost:8082/api/auth/users/by-email/";

    @Override
    @Transactional
    public void run(String... args) {
        if (taxpayerRepository.count() > 0) {
            log.info("Taxpayer data already exists. Skipping seeder.");
            return;
        }

        log.info("Seeding taxpayer profiles and documents...");

        Taxpayer rahul = seed("rahul.sharma@gmail.com", "Rahul Sharma",  "9876543210",
                "RHLSHR8801C", "ABCDE1234F", TaxpayerType.Citizen,
                "12 MG Road, Bengaluru, Karnataka 560001", "rahul.sharma@gmail.com | 9876543210");

        Taxpayer priya = seed("priya.nair@gmail.com",   "Priya Nair",    "9845012345",
                "PRYNIR9202C", "FGHIJ5678K", TaxpayerType.Citizen,
                "45 Anna Salai, Chennai, Tamil Nadu 600002", "priya.nair@gmail.com | 9845012345");

        Taxpayer arjun = seed("arjun.mehta@gmail.com",  "Arjun Mehta",   "9123456780",
                "ARJMHT8503B", "KLMNO9012P", TaxpayerType.Business,
                "78 Connaught Place, New Delhi 110001", "arjun.mehta@gmail.com | 9123456780");

        Taxpayer sneha = seed("sneha.reddy@gmail.com",  "Sneha Reddy",   "9988776655",
                "SNHRDY9104C", "QRSTU3456V", TaxpayerType.Citizen,
                "23 Banjara Hills, Hyderabad, Telangana 500034", "sneha.reddy@gmail.com | 9988776655");

        if (rahul != null) {
            saveDoc(rahul, DocType.PAN,     "https://storage.taxease.gov/docs/rahul/pan_card.pdf",     VerificationStatus.Verified);
            saveDoc(rahul, DocType.IDProof, "https://storage.taxease.gov/docs/rahul/aadhaar_card.pdf", VerificationStatus.Verified);
        }
        if (priya != null) {
            saveDoc(priya, DocType.PAN,     "https://storage.taxease.gov/docs/priya/pan_card.pdf",     VerificationStatus.Verified);
            saveDoc(priya, DocType.IDProof, "https://storage.taxease.gov/docs/priya/aadhaar_card.pdf", VerificationStatus.Verified);
        }
        if (arjun != null) {
            saveDoc(arjun, DocType.PAN,             "https://storage.taxease.gov/docs/arjun/pan_card.pdf",         VerificationStatus.Verified);
            saveDoc(arjun, DocType.IDProof,         "https://storage.taxease.gov/docs/arjun/aadhaar_card.pdf",     VerificationStatus.Verified);
            saveDoc(arjun, DocType.BusinessLicense, "https://storage.taxease.gov/docs/arjun/business_license.pdf", VerificationStatus.Verified);
        }
        if (sneha != null) {
            saveDoc(sneha, DocType.PAN,     "https://storage.taxease.gov/docs/sneha/pan_card.pdf",     VerificationStatus.Verified);
            saveDoc(sneha, DocType.IDProof, "https://storage.taxease.gov/docs/sneha/aadhaar_card.pdf", VerificationStatus.Pending);
        }

        log.info("Taxpayer seeding completed: {} taxpayers, {} documents.",
                taxpayerRepository.count(), documentRepository.count());
    }

    private Taxpayer seed(String email, String name, String phone,
                           String taxpayerIdNumber, String panNumber,
                           TaxpayerType type, String address, String contactInfo) {
        if (taxpayerRepository.existsByEmail(email)) {
            log.debug("Taxpayer {} already exists, skipping.", email);
            return taxpayerRepository.findByEmail(email).orElse(null);
        }
        Long userId = fetchUserId(email);
        if (userId == null) {
            log.warn("Could not find userId for email: {}. Skipping.", email);
            return null;
        }
        return taxpayerRepository.saveAndFlush(Taxpayer.builder()
                .userId(userId)
                .name(name)
                .email(email)
                .phone(phone)
                .taxpayerIdNumber(taxpayerIdNumber)
                .panNumber(panNumber)
                .type(type)
                .address(address)
                .contactInfo(contactInfo)
                .build());
    }

    private Long fetchUserId(String email) {
        try {
            Map<?, ?> response = restTemplate.getForObject(IDENTITY_URL + email, Map.class);
            if (response != null && response.get("userId") != null) {
                return ((Number) response.get("userId")).longValue();
            }
        } catch (Exception e) {
            log.warn("Could not fetch userId for {}: {}", email, e.getMessage());
        }
        return null;
    }

    private void saveDoc(Taxpayer taxpayer, DocType docType, String fileUri, VerificationStatus status) {
        documentRepository.saveAndFlush(TaxpayerDocument.builder()
                .taxpayer(taxpayer)
                .docType(docType)
                .fileUri(fileUri)
                .verificationStatus(status)
                .build());
    }
}
