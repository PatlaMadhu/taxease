package com.taxpayerservice.repository;

import com.taxpayerservice.entity.Taxpayer;
import com.taxpayerservice.entity.TaxpayerDocument;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaxpayerDocumentRepository extends JpaRepository<TaxpayerDocument, Long> {
    List<TaxpayerDocument> findByTaxpayer(Taxpayer taxpayer);
}
