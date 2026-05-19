package com.reportservice.repository;

import com.reportservice.entity.Report;
import com.reportservice.entity.enums.ReportScope;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReportRepository extends JpaRepository<Report, Long> {
    List<Report> findByScope(ReportScope scope);
    List<Report> findByGeneratedBy(Long generatedBy);
}
