package com.auditservice.dto;

import com.auditservice.entity.enums.ComplianceType;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class ComplianceResponse {
    private Long complianceId;
    private Long entityId;
    private ComplianceType type;
    private String result;
    private String notes;
    private Instant recordDate;
}
