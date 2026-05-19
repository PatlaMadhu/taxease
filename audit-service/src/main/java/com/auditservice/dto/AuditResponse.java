package com.auditservice.dto;

import com.auditservice.entity.enums.AuditStatus;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class AuditResponse {
    private Long auditId;
    private Long officerId;
    private Long taxpayerId;
    private String scope;
    private String findings;
    private AuditStatus status;
    private Instant createdAt;
}
