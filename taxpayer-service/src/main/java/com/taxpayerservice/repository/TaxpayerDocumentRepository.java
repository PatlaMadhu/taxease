package com.taxpayerservice.repository;

import com.taxpayerservice.entity.Taxpayer;
import com.taxpayerservice.entity.TaxpayerDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TaxpayerDocumentRepository extends JpaRepository<TaxpayerDocument, Long> {
    List<TaxpayerDocument> findByTaxpayer(Taxpayer taxpayer);

    @Query("SELECT d FROM TaxpayerDocument d JOIN FETCH d.taxpayer")
    List<TaxpayerDocument> findAllWithTaxpayer();
}
