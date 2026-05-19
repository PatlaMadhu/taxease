package com.taxfilingservice.repository;

import com.taxfilingservice.entity.TaxFiling;
import com.taxfilingservice.entity.enums.FilingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface TaxFilingRepository extends JpaRepository<TaxFiling, Long> {
    List<TaxFiling> findByTaxpayerId(Long taxpayerId);
    List<TaxFiling> findByStatus(FilingStatus status);
    List<TaxFiling> findByTaxpayerIdAndStatus(Long taxpayerId, FilingStatus status);

    @Modifying
    @Transactional
    @Query("UPDATE TaxFiling f SET f.status = com.taxfilingservice.entity.enums.FilingStatus.SUBMITTED WHERE f.status = com.taxfilingservice.entity.enums.FilingStatus.DRAFT")
    int migrateAllDraftsToSubmitted();
}
