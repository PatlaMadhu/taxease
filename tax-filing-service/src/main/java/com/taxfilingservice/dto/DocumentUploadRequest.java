package com.taxfilingservice.dto;

import com.taxfilingservice.entity.enums.FilingDocType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class DocumentUploadRequest {

    @NotNull
    private FilingDocType docType;

    @NotBlank
    private String fileUri;
}
