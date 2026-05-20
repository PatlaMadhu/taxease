package com.taxpayerservice.controller;

import com.taxpayerservice.dto.response.DocumentResponse;
import com.taxpayerservice.service.TaxpayerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/documents")
@RequiredArgsConstructor
@Slf4j
public class DocumentReviewController {

    private final TaxpayerService taxpayerService;

    @GetMapping
    public ResponseEntity<List<DocumentResponse>> getAllDocuments() {
        log.info("Fetching all documents for review");
        return ResponseEntity.ok(taxpayerService.getAllDocuments());
    }

    @PutMapping("/{documentId}/verify")
    public ResponseEntity<DocumentResponse> verifyDocument(
            @PathVariable Long documentId,
            @RequestParam String status) {
        log.info("Verifying document {} with status {}", documentId, status);
        return ResponseEntity.ok(taxpayerService.verifyDocument(documentId, status));
    }
}
