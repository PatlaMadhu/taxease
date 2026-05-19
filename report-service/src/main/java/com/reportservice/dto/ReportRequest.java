package com.reportservice.dto;

import com.reportservice.entity.enums.ReportScope;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ReportRequest {

    @NotNull
    private ReportScope scope;

    @NotBlank
    private String title;

    @NotBlank
    private String metrics;

    private Long generatedBy;
}
