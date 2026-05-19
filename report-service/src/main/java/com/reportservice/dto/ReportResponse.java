package com.reportservice.dto;

import com.reportservice.entity.enums.ReportScope;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class ReportResponse {
    private Long reportId;
    private ReportScope scope;
    private String title;
    private String metrics;
    private Long generatedBy;
    private Instant generatedDate;
}
