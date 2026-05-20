package com.taxpayerservice.controller;

import com.taxpayerservice.dto.request.DocumentUpdateRequest;
import com.taxpayerservice.dto.request.DocumentUploadRequest;
import com.taxpayerservice.dto.request.UpdateProfileRequest;
import com.taxpayerservice.dto.response.DocumentResponse;
import com.taxpayerservice.dto.response.TaxpayerProfileResponse;
import com.taxpayerservice.event.TaxpayerRegisteredEvent;
import com.taxpayerservice.service.TaxpayerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/taxpayers")
@RequiredArgsConstructor
@Slf4j
public class TaxpayerController {

    private final TaxpayerService taxpayerService;

    @GetMapping("/profile")
    public ResponseEntity<TaxpayerProfileResponse> getProfile(
            @RequestHeader("X-User-Email") String email) {
        log.info("Fetching profile for: {}", email);
        return ResponseEntity.ok(taxpayerService.getProfileByEmail(email));
    }

    @GetMapping("/{taxpayerId}")
    public ResponseEntity<TaxpayerProfileResponse> getProfileById(@PathVariable Long taxpayerId) {
        return ResponseEntity.ok(taxpayerService.getProfileById(taxpayerId));
    }

    @PutMapping("/profile")
    public ResponseEntity<TaxpayerProfileResponse> updateProfile(
            @RequestHeader("X-User-Email") String email,
            @Valid @RequestBody UpdateProfileRequest request) {
        log.info("Updating profile for: {}", email);
        return ResponseEntity.ok(taxpayerService.updateProfile(email, request));
    }

    @GetMapping("/documents")
    public ResponseEntity<List<DocumentResponse>> getDocuments(
            @RequestHeader("X-User-Email") String email) {
        return ResponseEntity.ok(taxpayerService.getDocuments(email));
    }

    @PostMapping("/documents")
    public ResponseEntity<DocumentResponse> uploadDocument(
            @RequestHeader("X-User-Email") String email,
            @Valid @RequestBody DocumentUploadRequest request) {
        log.info("Uploading document type: {} for: {}", request.getDocType(), email);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(taxpayerService.uploadDocument(email, request));
    }

    @PutMapping("/documents/{documentId}")
    public ResponseEntity<DocumentResponse> updateDocument(
            @RequestHeader("X-User-Email") String email,
            @PathVariable Long documentId,
            @Valid @RequestBody DocumentUpdateRequest request) {
        log.info("Updating document: {} for: {}", documentId, email);
        return ResponseEntity.ok(taxpayerService.updateDocument(email, documentId, request));
    }

    @DeleteMapping("/documents/{documentId}")
    public ResponseEntity<Void> deleteDocument(
            @RequestHeader("X-User-Email") String email,
            @PathVariable Long documentId) {
        log.info("Deleting document: {} for: {}", documentId, email);
        taxpayerService.deleteDocument(email, documentId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/internal/create")
    public ResponseEntity<TaxpayerProfileResponse> createProfile(
            @RequestBody TaxpayerRegisteredEvent event) {
        log.info("Internal: creating taxpayer profile for userId: {}", event.getUserId());
        return ResponseEntity.status(HttpStatus.CREATED).body(taxpayerService.createProfile(event));
    }

    @GetMapping("/by-email/{email}")
    public ResponseEntity<TaxpayerProfileResponse> getByEmail(@PathVariable String email) {
        return ResponseEntity.ok(taxpayerService.getProfileByEmail(email));
    }

}
