package com.taxfilingservice.dto;

import com.taxfilingservice.entity.enums.FilingStatus;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@Builder
public class FilingResponse {
    private Long filingId;
    private Long taxpayerId;
    private String taxpayerEmail;
    private String period;
    private BigDecimal amountDeclared;
    private FilingStatus status;
    private Instant submittedDate;
}
