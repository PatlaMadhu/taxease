package com.auditservice.dto;

import com.auditservice.entity.enums.ComplianceType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ComplianceRequest {

    @NotNull
    private Long entityId;

    @NotNull
    private ComplianceType type;

    @NotBlank
    private String result;

    private String notes;
}
