package com.taxfilingservice.service;

import com.taxfilingservice.dto.DocumentResponse;
import com.taxfilingservice.dto.DocumentUploadRequest;
import com.taxfilingservice.dto.FilingRequest;
import com.taxfilingservice.dto.FilingResponse;
import com.taxfilingservice.entity.enums.FilingStatus;

import java.util.List;

public interface TaxFilingService {
    List<FilingResponse> getAllFilings();
    FilingResponse createFiling(FilingRequest request);
    FilingResponse getFilingById(Long filingId);
    List<FilingResponse> getFilingsByTaxpayer(Long taxpayerId);
    List<FilingResponse> getFilingsByStatus(FilingStatus status);
    FilingResponse submitFiling(Long filingId);
    FilingResponse updateFilingStatus(Long filingId, FilingStatus status);
    DocumentResponse uploadDocument(Long filingId, DocumentUploadRequest request);
    List<DocumentResponse> getDocuments(Long filingId);
}
