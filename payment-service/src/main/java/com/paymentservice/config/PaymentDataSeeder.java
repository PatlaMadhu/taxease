package com.paymentservice.config;

import com.paymentservice.entity.Payment;
import com.paymentservice.entity.enums.PaymentMethod;
import com.paymentservice.entity.enums.PaymentStatus;
import com.paymentservice.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class PaymentDataSeeder implements CommandLineRunner {

    private final PaymentRepository paymentRepository;
    private final RestTemplate restTemplate;

    private static final String FILING_URL = "http://localhost:8083/api/filings/taxpayer/";

    @Override
    @Transactional
    public void run(String... args) {
        if (paymentRepository.count() > 0) {
            log.info("Payment data already exists. Skipping seeder.");
            return;
        }

        log.info("Seeding payment records...");

        // Fetch filings per taxpayer and find the APPROVED ones to pay
        seedPaymentsForTaxpayer(1L, new BigDecimal("48500.00"),  PaymentMethod.NetBanking, "2022-23");
        seedPaymentsForTaxpayer(2L, new BigDecimal("32000.00"),  PaymentMethod.UPI_GPAY,   "2022-23");
        seedPaymentsForTaxpayer(3L, new BigDecimal("375000.00"), PaymentMethod.Bank,        "2022-23");
        seedPaymentsForTaxpayer(4L, new BigDecimal("41000.00"),  PaymentMethod.UPI_PHONEPE, "2023-24");

        log.info("Payment seeding completed: {} payment records.", paymentRepository.count());
    }

    private void seedPaymentsForTaxpayer(Long taxpayerId, BigDecimal amount,
                                          PaymentMethod method, String period) {
        try {
            List<Map<String, Object>> filings = restTemplate.exchange(
                    FILING_URL + taxpayerId,
                    HttpMethod.GET, null,
                    new ParameterizedTypeReference<List<Map<String, Object>>>() {}
            ).getBody();

            if (filings == null) return;

            filings.stream()
                    .filter(f -> period.equals(f.get("period")) && "APPROVED".equals(f.get("status")))
                    .findFirst()
                    .ifPresent(f -> {
                        Long filingId = ((Number) f.get("filingId")).longValue();
                        paymentRepository.saveAndFlush(Payment.builder()
                                .filingId(filingId)
                                .taxpayerId(taxpayerId)
                                .amount(amount)
                                .method(method)
                                .status(PaymentStatus.Completed)
                                .build());
                        log.info("Seeded payment for taxpayerId={} filingId={}", taxpayerId, filingId);
                    });
        } catch (Exception e) {
            log.warn("Could not seed payment for taxpayerId={}: {}", taxpayerId, e.getMessage());
        }
    }
}
