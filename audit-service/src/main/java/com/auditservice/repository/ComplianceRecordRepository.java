package com.auditservice.repository;

import com.auditservice.entity.ComplianceRecord;
import com.auditservice.entity.enums.ComplianceType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ComplianceRecordRepository extends JpaRepository<ComplianceRecord, Long> {
    List<ComplianceRecord> findByEntityId(Long entityId);
    List<ComplianceRecord> findByType(ComplianceType type);
    List<ComplianceRecord> findByResultIgnoreCase(String result);
    long countByResultIgnoreCase(String result);
}
