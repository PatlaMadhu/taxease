package com.taxpayerservice.service.impl;

import com.taxpayerservice.dto.request.DocumentUpdateRequest;
import com.taxpayerservice.dto.request.DocumentUploadRequest;
import com.taxpayerservice.dto.request.UpdateProfileRequest;
import com.taxpayerservice.dto.response.DocumentResponse;
import com.taxpayerservice.dto.response.TaxpayerProfileResponse;
import com.taxpayerservice.entity.Taxpayer;
import com.taxpayerservice.entity.TaxpayerDocument;
import com.taxpayerservice.entity.enums.TaxpayerType;
import com.taxpayerservice.entity.enums.VerificationStatus;
import com.taxpayerservice.event.TaxpayerRegisteredEvent;
import com.taxpayerservice.repository.TaxpayerDocumentRepository;
import com.taxpayerservice.repository.TaxpayerRepository;
import com.taxpayerservice.service.TaxpayerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Slf4j
public class TaxpayerServiceImpl implements TaxpayerService {

    private final TaxpayerRepository taxpayerRepository;
    private final TaxpayerDocumentRepository documentRepository;

    @Override
    public TaxpayerProfileResponse getProfileByEmail(String email) {
        return toResponse(findByEmail(email));
    }

    @Override
    public TaxpayerProfileResponse getProfileById(Long taxpayerId) {
        return toResponse(taxpayerRepository.findById(taxpayerId)
                .orElseThrow(() -> new NoSuchElementException("Taxpayer not found: " + taxpayerId)));
    }

    @Override
    @Transactional
    public TaxpayerProfileResponse updateProfile(String email, UpdateProfileRequest request) {
        Taxpayer taxpayer = findByEmail(email);
        if (request.getAddress() != null) taxpayer.setAddress(request.getAddress());
        if (request.getContactInfo() != null) taxpayer.setContactInfo(request.getContactInfo());
        if (request.getPanNumber() != null) taxpayer.setPanNumber(request.getPanNumber());
        return toResponse(taxpayerRepository.save(taxpayer));
    }

    @Override
    @Transactional
    public DocumentResponse uploadDocument(String email, DocumentUploadRequest request) {
        Taxpayer taxpayer = findByEmail(email);
        boolean exists = documentRepository.findByTaxpayer(taxpayer)
                .stream().anyMatch(d -> d.getDocType() == request.getDocType());
        if (exists) {
            throw new IllegalArgumentException("Document of type " + request.getDocType() + " already exists");
        }
        TaxpayerDocument doc = TaxpayerDocument.builder()
                .taxpayer(taxpayer)
                .docType(request.getDocType())
                .fileUri(request.getFileUri())
                .verificationStatus(VerificationStatus.Pending)
                .build();
        return toDocResponse(documentRepository.save(doc));
    }

    @Override
    public List<DocumentResponse> getDocuments(String email) {
        Taxpayer taxpayer = findByEmail(email);
        return documentRepository.findByTaxpayer(taxpayer).stream().map(this::toDocResponse).toList();
    }

    @Override
    @Transactional
    public DocumentResponse updateDocument(String email, Long documentId, DocumentUpdateRequest request) {
        Taxpayer taxpayer = findByEmail(email);
        TaxpayerDocument doc = findDocument(documentId, taxpayer);
        doc.setFileUri(request.getFileUri());
        doc.setVerificationStatus(VerificationStatus.Pending);
        return toDocResponse(documentRepository.save(doc));
    }

    @Override
    @Transactional
    public void deleteDocument(String email, Long documentId) {
        Taxpayer taxpayer = findByEmail(email);
        TaxpayerDocument doc = findDocument(documentId, taxpayer);
        documentRepository.delete(doc);
    }

    private Taxpayer findByEmail(String email) {
        return taxpayerRepository.findByEmail(email)
                .orElseThrow(() -> new NoSuchElementException("Taxpayer not found for email: " + email));
    }

    @Override
    @Transactional
    public TaxpayerProfileResponse createProfile(TaxpayerRegisteredEvent event) {
        if (taxpayerRepository.existsByEmail(event.getEmail())) {
            return toResponse(taxpayerRepository.findByEmail(event.getEmail()).get());
        }
        TaxpayerType type = Arrays.stream(TaxpayerType.values())
                .filter(t -> t.name().equalsIgnoreCase(event.getTaxpayerType()))
                .findFirst().orElse(TaxpayerType.Citizen);
        Taxpayer taxpayer = Taxpayer.builder()
                .userId(event.getUserId())
                .name(event.getName())
                .email(event.getEmail())
                .phone(event.getPhone())
                .type(type)
                .address(event.getAddress())
                .contactInfo(event.getContactInfo())
                .build();
        return toResponse(taxpayerRepository.save(taxpayer));
    }

    private TaxpayerDocument findDocument(Long documentId, Taxpayer taxpayer) {
        TaxpayerDocument doc = documentRepository.findById(documentId)
                .orElseThrow(() -> new NoSuchElementException("Document not found: " + documentId));
        if (!doc.getTaxpayer().getId().equals(taxpayer.getId())) {
            throw new IllegalArgumentException("Document does not belong to this taxpayer");
        }
        return doc;
    }

    private TaxpayerProfileResponse toResponse(Taxpayer t) {
        return TaxpayerProfileResponse.builder()
                .taxpayerId(t.getId())
                .userId(t.getUserId())
                .name(t.getName())
                .email(t.getEmail())
                .phone(t.getPhone())
                .taxpayerIdNumber(t.getTaxpayerIdNumber())
                .panNumber(t.getPanNumber())
                .type(t.getType())
                .address(t.getAddress())
                .contactInfo(t.getContactInfo())
                .createdAt(t.getCreatedAt())
                .build();
    }

    private DocumentResponse toDocResponse(TaxpayerDocument d) {
        return DocumentResponse.builder()
                .documentId(d.getId())
                .taxpayerId(d.getTaxpayer().getId())
                .docType(d.getDocType())
                .fileUri(d.getFileUri())
                .verificationStatus(d.getVerificationStatus())
                .uploadedDate(d.getUploadedDate())
                .updatedAt(d.getUpdatedAt())
                .build();
    }
}
