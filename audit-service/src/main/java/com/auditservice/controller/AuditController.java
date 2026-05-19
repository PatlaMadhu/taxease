package com.auditservice.controller;

import com.auditservice.dto.AuditDashboardResponse;
import com.auditservice.dto.AuditRequest;
import com.auditservice.dto.AuditResponse;
import com.auditservice.dto.ComplianceRequest;
import com.auditservice.dto.ComplianceResponse;
import com.auditservice.entity.enums.AuditStatus;
import com.auditservice.service.AuditService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/audits")
@RequiredArgsConstructor
@Slf4j
public class AuditController {

    private final AuditService auditService;

    @PostMapping
    public ResponseEntity<AuditResponse> createAudit(@Valid @RequestBody AuditRequest request) {
        log.info("Creating audit for officer: {}", request.getOfficerId());
        return ResponseEntity.status(HttpStatus.CREATED).body(auditService.createAudit(request));
    }

    @GetMapping
    public ResponseEntity<List<AuditResponse>> getAllAudits() {
        return ResponseEntity.ok(auditService.getAllAudits());
    }

    @GetMapping("/{auditId}")
    public ResponseEntity<AuditResponse> getAuditById(@PathVariable Long auditId) {
        return ResponseEntity.ok(auditService.getAuditById(auditId));
    }

    @GetMapping("/officer/{officerId}")
    public ResponseEntity<List<AuditResponse>> getAuditsByOfficer(@PathVariable Long officerId) {
        return ResponseEntity.ok(auditService.getAuditsByOfficer(officerId));
    }

    @PutMapping("/{auditId}/close")
    public ResponseEntity<AuditResponse> closeAudit(
            @PathVariable Long auditId,
            @RequestParam String findings) {
        log.info("Closing audit: {}", auditId);
        return ResponseEntity.ok(auditService.closeAudit(auditId, findings));
    }

    @PutMapping("/{auditId}/status")
    public ResponseEntity<AuditResponse> updateStatus(
            @PathVariable Long auditId,
            @RequestParam AuditStatus status) {
        return ResponseEntity.ok(auditService.updateAuditStatus(auditId, status));
    }

    @PostMapping("/compliance")
    public ResponseEntity<ComplianceResponse> createCompliance(@Valid @RequestBody ComplianceRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(auditService.createComplianceRecord(request));
    }

    @GetMapping("/compliance")
    public ResponseEntity<List<ComplianceResponse>> getAllCompliance() {
        return ResponseEntity.ok(auditService.getAllCompliance());
    }

    @GetMapping("/compliance/entity/{entityId}")
    public ResponseEntity<List<ComplianceResponse>> getComplianceByEntity(@PathVariable Long entityId) {
        return ResponseEntity.ok(auditService.getComplianceByEntity(entityId));
    }

    @GetMapping("/dashboard")
    public ResponseEntity<AuditDashboardResponse> getAuditDashboard() {
        log.info("Fetching audit dashboard");
        return ResponseEntity.ok(auditService.getAuditDashboard());
    }
}
