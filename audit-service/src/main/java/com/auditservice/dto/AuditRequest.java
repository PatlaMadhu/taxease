package com.auditservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AuditRequest {

    @NotNull
    private Long officerId;

    private Long taxpayerId;

    @NotBlank
    private String scope;
}
