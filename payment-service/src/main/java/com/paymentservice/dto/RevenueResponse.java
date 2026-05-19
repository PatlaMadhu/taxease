package com.paymentservice.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@Builder
public class RevenueResponse {
    private Long revenueId;
    private Long paymentId;
    private Long taxpayerId;
    private BigDecimal amount;
    private String status;
    private Instant recordDate;
}
