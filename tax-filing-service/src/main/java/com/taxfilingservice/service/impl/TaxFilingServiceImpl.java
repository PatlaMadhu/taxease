package com.taxfilingservice.service.impl;

import com.taxfilingservice.dto.DocumentResponse;
import com.taxfilingservice.dto.DocumentUploadRequest;
import com.taxfilingservice.dto.FilingRequest;
import com.taxfilingservice.dto.FilingResponse;
import com.taxfilingservice.entity.FilingDocument;
import com.taxfilingservice.entity.TaxFiling;
import com.taxfilingservice.entity.enums.FilingStatus;
import com.taxfilingservice.entity.enums.VerificationStatus;
import com.taxfilingservice.event.FilingEventPublisher;
import com.taxfilingservice.event.FilingSubmittedEvent;
import com.taxfilingservice.repository.FilingDocumentRepository;
import com.taxfilingservice.repository.TaxFilingRepository;
import com.taxfilingservice.service.TaxFilingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Slf4j
public class TaxFilingServiceImpl implements TaxFilingService {

    private final TaxFilingRepository filingRepository;
    private final FilingDocumentRepository documentRepository;
    private final FilingEventPublisher eventPublisher;

    @Override
    public List<FilingResponse> getAllFilings() {
        return filingRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Override
    @Transactional
    public FilingResponse createFiling(FilingRequest request) {
        log.info("Creating and submitting filing for taxpayerId: {}", request.getTaxpayerId());
        TaxFiling filing = TaxFiling.builder()
                .taxpayerId(request.getTaxpayerId())
                .userId(request.getUserId())
                .taxpayerEmail(request.getTaxpayerEmail())
                .period(request.getPeriod())
                .amountDeclared(request.getAmountDeclared())
                .status(FilingStatus.SUBMITTED)
                .build();
        TaxFiling saved = filingRepository.saveAndFlush(filing);
        FilingResponse response = toResponse(saved);
        eventPublisher.publishFilingSubmitted(FilingSubmittedEvent.builder()
                .filingId(saved.getId())
                .taxpayerId(saved.getTaxpayerId())
                .userId(saved.getUserId())
                .taxpayerEmail(saved.getTaxpayerEmail())
                .period(saved.getPeriod())
                .status(saved.getStatus().name())
                .build());
        return response;
    }

    @Override
    public FilingResponse getFilingById(Long filingId) {
        return toResponse(findFiling(filingId));
    }

    @Override
    public List<FilingResponse> getFilingsByTaxpayer(Long taxpayerId) {
        return filingRepository.findByTaxpayerId(taxpayerId).stream().map(this::toResponse).toList();
    }

    @Override
    public List<FilingResponse> getFilingsByStatus(FilingStatus status) {
        return filingRepository.findByStatus(status).stream().map(this::toResponse).toList();
    }

    @Override
    @Transactional
    public FilingResponse submitFiling(Long filingId) {
        TaxFiling filing = findFiling(filingId);
        if (filing.getStatus() != FilingStatus.DRAFT) {
            throw new IllegalStateException("Only DRAFT filings can be submitted");
        }
        filing.setStatus(FilingStatus.SUBMITTED);
        TaxFiling saved = filingRepository.save(filing);
        eventPublisher.publishFilingSubmitted(FilingSubmittedEvent.builder()
                .filingId(saved.getId())
                .taxpayerId(saved.getTaxpayerId())
                .userId(saved.getUserId())
                .taxpayerEmail(saved.getTaxpayerEmail())
                .period(saved.getPeriod())
                .status(saved.getStatus().name())
                .build());
        return toResponse(saved);
    }

    @Override
    @Transactional
    public FilingResponse updateFilingStatus(Long filingId, FilingStatus status) {
        TaxFiling filing = findFiling(filingId);
        filing.setStatus(status);
        return toResponse(filingRepository.save(filing));
    }

    @Override
    @Transactional
    public DocumentResponse uploadDocument(Long filingId, DocumentUploadRequest request) {
        TaxFiling filing = findFiling(filingId);
        FilingDocument doc = FilingDocument.builder()
                .taxFiling(filing)
                .docType(request.getDocType())
                .fileUri(request.getFileUri())
                .verificationStatus(VerificationStatus.PENDING)
                .build();
        return toDocResponse(documentRepository.save(doc));
    }

    @Override
    public List<DocumentResponse> getDocuments(Long filingId) {
        TaxFiling filing = findFiling(filingId);
        return documentRepository.findByTaxFiling(filing).stream().map(this::toDocResponse).toList();
    }

    private TaxFiling findFiling(Long id) {
        return filingRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Filing not found: " + id));
    }

    private FilingResponse toResponse(TaxFiling f) {
        return FilingResponse.builder()
                .filingId(f.getId())
                .taxpayerId(f.getTaxpayerId())
                .taxpayerEmail(f.getTaxpayerEmail())
                .period(f.getPeriod())
                .amountDeclared(f.getAmountDeclared())
                .status(f.getStatus())
                .submittedDate(f.getSubmittedDate())
                .build();
    }

    private DocumentResponse toDocResponse(FilingDocument d) {
        return DocumentResponse.builder()
                .documentId(d.getId())
                .filingId(d.getTaxFiling().getId())
                .docType(d.getDocType())
                .fileUri(d.getFileUri())
                .verificationStatus(d.getVerificationStatus())
                .uploadedDate(d.getUploadedDate())
                .build();
    }
}
