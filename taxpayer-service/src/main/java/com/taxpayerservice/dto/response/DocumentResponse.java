package com.taxpayerservice.dto.response;

import com.taxpayerservice.entity.enums.DocType;
import com.taxpayerservice.entity.enums.VerificationStatus;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class DocumentResponse {
    private Long documentId;
    private Long taxpayerId;
    private DocType docType;
    private String fileUri;
    private VerificationStatus verificationStatus;
    private Instant uploadedDate;
    private Instant updatedAt;
}
