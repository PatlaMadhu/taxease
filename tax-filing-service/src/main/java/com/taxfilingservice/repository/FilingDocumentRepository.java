package com.taxfilingservice.repository;

import com.taxfilingservice.entity.FilingDocument;
import com.taxfilingservice.entity.TaxFiling;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FilingDocumentRepository extends JpaRepository<FilingDocument, Long> {
    List<FilingDocument> findByTaxFiling(TaxFiling taxFiling);
}
