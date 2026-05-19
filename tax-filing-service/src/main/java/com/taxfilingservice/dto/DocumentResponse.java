package com.taxfilingservice.dto;

import com.taxfilingservice.entity.enums.FilingDocType;
import com.taxfilingservice.entity.enums.VerificationStatus;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class DocumentResponse {
    private Long documentId;
    private Long filingId;
    private FilingDocType docType;
    private String fileUri;
    private VerificationStatus verificationStatus;
    private Instant uploadedDate;
}
