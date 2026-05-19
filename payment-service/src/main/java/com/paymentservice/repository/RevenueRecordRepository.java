package com.paymentservice.repository;

import com.paymentservice.entity.RevenueRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface RevenueRecordRepository extends JpaRepository<RevenueRecord, Long> {
    List<RevenueRecord> findByTaxpayerId(Long taxpayerId);

    @Query("SELECT SUM(r.amount) FROM RevenueRecord r WHERE r.status = :status")
    BigDecimal sumCollectedRevenue(@Param("status") String status);
}
