package com.paymentservice.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class RevenueDashboardResponse {
    private BigDecimal totalRevenue;
    private BigDecimal successfulRevenue;
    private BigDecimal pendingRevenue;
    private long totalTransactions;
}
