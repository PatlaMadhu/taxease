package com.auditservice.service.impl;

import com.auditservice.dto.AuditDashboardResponse;
import com.auditservice.dto.AuditRequest;
import com.auditservice.dto.AuditResponse;
import com.auditservice.dto.ComplianceRequest;
import com.auditservice.dto.ComplianceResponse;
import com.auditservice.entity.AuditCase;
import com.auditservice.entity.ComplianceRecord;
import com.auditservice.entity.enums.AuditStatus;
import com.auditservice.repository.AuditCaseRepository;
import com.auditservice.repository.ComplianceRecordRepository;
import com.auditservice.service.AuditService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuditServiceImpl implements AuditService {

    private final AuditCaseRepository auditCaseRepository;
    private final ComplianceRecordRepository complianceRecordRepository;

    @Override
    @Transactional
    public AuditResponse createAudit(AuditRequest request) {
        log.info("Creating audit for officerId: {}", request.getOfficerId());
        AuditCase audit = AuditCase.builder()
                .officerId(request.getOfficerId())
                .taxpayerId(request.getTaxpayerId())
                .scope(request.getScope())
                .status(AuditStatus.OPEN)
                .build();
        return toResponse(auditCaseRepository.save(audit));
    }

    @Override
    public AuditResponse getAuditById(Long auditId) {
        return toResponse(findAudit(auditId));
    }

    @Override
    public List<AuditResponse> getAllAudits() {
        return auditCaseRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Override
    public List<AuditResponse> getAuditsByOfficer(Long officerId) {
        return auditCaseRepository.findByOfficerId(officerId).stream().map(this::toResponse).toList();
    }

    @Override
    @Transactional
    public AuditResponse closeAudit(Long auditId, String findings) {
        AuditCase audit = findAudit(auditId);
        audit.setFindings(findings);
        audit.setStatus(AuditStatus.CLOSED);
        return toResponse(auditCaseRepository.save(audit));
    }

    @Override
    @Transactional
    public AuditResponse updateAuditStatus(Long auditId, AuditStatus status) {
        AuditCase audit = findAudit(auditId);
        audit.setStatus(status);
        return toResponse(auditCaseRepository.save(audit));
    }

    @Override
    @Transactional
    public ComplianceResponse createComplianceRecord(ComplianceRequest request) {
        ComplianceRecord record = ComplianceRecord.builder()
                .entityId(request.getEntityId())
                .type(request.getType())
                .result(request.getResult())
                .notes(request.getNotes())
                .build();
        return toComplianceResponse(complianceRecordRepository.save(record));
    }

    @Override
    public List<ComplianceResponse> getAllCompliance() {
        return complianceRecordRepository.findAll().stream().map(this::toComplianceResponse).toList();
    }

    @Override
    public List<ComplianceResponse> getComplianceByEntity(Long entityId) {
        return complianceRecordRepository.findByEntityId(entityId).stream()
                .map(this::toComplianceResponse).toList();
    }

    @Override
    public AuditDashboardResponse getAuditDashboard() {
        return AuditDashboardResponse.builder()
                .totalAudits(auditCaseRepository.count())
                .openAudits(auditCaseRepository.countByStatus(AuditStatus.OPEN))
                .closedAudits(auditCaseRepository.countByStatus(AuditStatus.CLOSED))
                .escalatedAudits(auditCaseRepository.countByStatus(AuditStatus.ESCALATED))
                .nonComplianceFilings(complianceRecordRepository.countByResultIgnoreCase("Non-Compliant"))
                .build();
    }

    private AuditCase findAudit(Long id) {
        return auditCaseRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Audit not found: " + id));
    }

    private AuditResponse toResponse(AuditCase a) {
        return AuditResponse.builder()
                .auditId(a.getId())
                .officerId(a.getOfficerId())
                .taxpayerId(a.getTaxpayerId())
                .scope(a.getScope())
                .findings(a.getFindings())
                .status(a.getStatus())
                .createdAt(a.getCreatedAt())
                .build();
    }

    private ComplianceResponse toComplianceResponse(ComplianceRecord r) {
        return ComplianceResponse.builder()
                .complianceId(r.getId())
                .entityId(r.getEntityId())
                .type(r.getType())
                .result(r.getResult())
                .notes(r.getNotes())
                .recordDate(r.getRecordDate())
                .build();
    }
}
