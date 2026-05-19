package com.auditservice.repository;

import com.auditservice.entity.AuditCase;
import com.auditservice.entity.enums.AuditStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AuditCaseRepository extends JpaRepository<AuditCase, Long> {
    List<AuditCase> findByOfficerId(Long officerId);
    List<AuditCase> findByTaxpayerId(Long taxpayerId);
    List<AuditCase> findByStatus(AuditStatus status);
    long countByStatus(AuditStatus status);
}
