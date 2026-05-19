package com.reportservice.client;

import com.reportservice.dto.PaymentMetricsResponse;
import com.reportservice.dto.RevenueDashboardResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@Slf4j
public class PaymentClientFallback implements PaymentClient {

    @Override
    public PaymentMetricsResponse getPaymentMetrics() {
        log.warn("payment-service unavailable, returning fallback metrics");
        return PaymentMetricsResponse.builder()
                .successfulTransactions(0L)
                .failedTransactions(0L)
                .totalTransactions(0L)
                .build();
    }

    @Override
    public RevenueDashboardResponse getRevenueDashboard() {
        log.warn("payment-service unavailable, returning fallback revenue");
        return RevenueDashboardResponse.builder()
                .totalRevenue(BigDecimal.ZERO)
                .successfulRevenue(BigDecimal.ZERO)
                .pendingRevenue(BigDecimal.ZERO)
                .totalTransactions(0L)
                .build();
    }
}
