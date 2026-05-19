package com.taxfilingservice.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class FilingRequest {

    @NotNull
    private Long taxpayerId;

    private Long userId;

    @NotBlank
    private String taxpayerEmail;

    @NotBlank
    private String period;

    @NotNull
    @DecimalMin("0.0")
    private BigDecimal amountDeclared;
}
