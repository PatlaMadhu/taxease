package com.taxpayerservice.service;

import com.taxpayerservice.dto.request.DocumentUpdateRequest;
import com.taxpayerservice.dto.request.DocumentUploadRequest;
import com.taxpayerservice.dto.request.UpdateProfileRequest;
import com.taxpayerservice.dto.response.DocumentResponse;
import com.taxpayerservice.dto.response.TaxpayerProfileResponse;
import com.taxpayerservice.event.TaxpayerRegisteredEvent;

import java.util.List;

public interface TaxpayerService {
    TaxpayerProfileResponse getProfileByEmail(String email);
    TaxpayerProfileResponse getProfileById(Long taxpayerId);
    TaxpayerProfileResponse updateProfile(String email, UpdateProfileRequest request);
    DocumentResponse uploadDocument(String email, DocumentUploadRequest request);
    List<DocumentResponse> getDocuments(String email);
    DocumentResponse updateDocument(String email, Long documentId, DocumentUpdateRequest request);
    void deleteDocument(String email, Long documentId);
    TaxpayerProfileResponse createProfile(TaxpayerRegisteredEvent event);
}
