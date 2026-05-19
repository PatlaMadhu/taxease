package com.taxfilingservice.controller;

import com.taxfilingservice.dto.DocumentResponse;
import com.taxfilingservice.dto.DocumentUploadRequest;
import com.taxfilingservice.dto.FilingRequest;
import com.taxfilingservice.dto.FilingResponse;
import com.taxfilingservice.entity.enums.FilingStatus;
import com.taxfilingservice.service.TaxFilingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/filings")
@RequiredArgsConstructor
@Slf4j
public class TaxFilingController {

    private final TaxFilingService filingService;

    @GetMapping
    public ResponseEntity<List<FilingResponse>> getAllFilings() {
        return ResponseEntity.ok(filingService.getAllFilings());
    }

    @PostMapping
    public ResponseEntity<FilingResponse> createFiling(@Valid @RequestBody FilingRequest request) {
        log.info("Creating filing for taxpayer: {}", request.getTaxpayerId());
        return ResponseEntity.status(HttpStatus.CREATED).body(filingService.createFiling(request));
    }

    @GetMapping("/{filingId}")
    public ResponseEntity<FilingResponse> getFilingById(@PathVariable Long filingId) {
        return ResponseEntity.ok(filingService.getFilingById(filingId));
    }

    @GetMapping("/taxpayer/{taxpayerId}")
    public ResponseEntity<List<FilingResponse>> getFilingsByTaxpayer(@PathVariable Long taxpayerId) {
        return ResponseEntity.ok(filingService.getFilingsByTaxpayer(taxpayerId));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<FilingResponse>> getFilingsByStatus(@PathVariable FilingStatus status) {
        return ResponseEntity.ok(filingService.getFilingsByStatus(status));
    }

    @PutMapping("/{filingId}/submit")
    public ResponseEntity<FilingResponse> submitFiling(@PathVariable Long filingId) {
        log.info("Submitting filing: {}", filingId);
        return ResponseEntity.ok(filingService.submitFiling(filingId));
    }

    @PutMapping("/{filingId}/status")
    public ResponseEntity<FilingResponse> updateStatus(
            @PathVariable Long filingId,
            @RequestParam FilingStatus status) {
        log.info("Updating filing {} status to {}", filingId, status);
        return ResponseEntity.ok(filingService.updateFilingStatus(filingId, status));
    }

    @PostMapping("/{filingId}/documents")
    public ResponseEntity<DocumentResponse> uploadDocument(
            @PathVariable Long filingId,
            @Valid @RequestBody DocumentUploadRequest request) {
        log.info("Uploading document for filing: {}", filingId);
        return ResponseEntity.status(HttpStatus.CREATED).body(filingService.uploadDocument(filingId, request));
    }

    @GetMapping("/{filingId}/documents")
    public ResponseEntity<List<DocumentResponse>> getDocuments(@PathVariable Long filingId) {
        return ResponseEntity.ok(filingService.getDocuments(filingId));
    }
}
