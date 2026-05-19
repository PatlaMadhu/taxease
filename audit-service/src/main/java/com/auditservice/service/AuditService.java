package com.auditservice.service;

import com.auditservice.dto.AuditDashboardResponse;
import com.auditservice.dto.AuditRequest;
import com.auditservice.dto.AuditResponse;
import com.auditservice.dto.ComplianceRequest;
import com.auditservice.dto.ComplianceResponse;
import com.auditservice.entity.enums.AuditStatus;

import java.util.List;

public interface AuditService {
    AuditResponse createAudit(AuditRequest request);
    AuditResponse getAuditById(Long auditId);
    List<AuditResponse> getAllAudits();
    List<AuditResponse> getAuditsByOfficer(Long officerId);
    AuditResponse closeAudit(Long auditId, String findings);
    AuditResponse updateAuditStatus(Long auditId, AuditStatus status);
    ComplianceResponse createComplianceRecord(ComplianceRequest request);
    List<ComplianceResponse> getAllCompliance();
    List<ComplianceResponse> getComplianceByEntity(Long entityId);
    AuditDashboardResponse getAuditDashboard();
}
