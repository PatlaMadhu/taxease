package com.taxpayerservice.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class DocumentUpdateRequest {

    @NotBlank(message = "File URI is required")
    private String fileUri;
}
