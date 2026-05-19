package com.taxpayerservice.dto.request;

import com.taxpayerservice.entity.enums.DocType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class DocumentUploadRequest {

    @NotBlank(message = "File URI is required")
    private String fileUri;

    @NotNull(message = "Document type is required")
    private DocType docType;
}
