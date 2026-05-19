package com.paymentservice.dto;

import com.paymentservice.entity.enums.PaymentMethod;
import com.paymentservice.entity.enums.PaymentStatus;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@Builder
public class PaymentResponse {
    private Long paymentId;
    private Long filingId;
    private Long taxpayerId;
    private BigDecimal amount;
    private PaymentMethod method;
    private PaymentStatus status;
    private Instant paymentDate;
}
