package com.paymentservice.repository;

import com.paymentservice.entity.Payment;
import com.paymentservice.entity.enums.PaymentMethod;
import com.paymentservice.entity.enums.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    List<Payment> findByFilingId(Long filingId);
    List<Payment> findByTaxpayerId(Long taxpayerId);
    List<Payment> findByStatus(PaymentStatus status);
    long countByStatus(PaymentStatus status);
    long countByStatusAndMethod(PaymentStatus status, PaymentMethod method);
    long countByMethod(PaymentMethod method);

    @Query("SELECT SUM(p.amount) FROM Payment p WHERE p.status = :status")
    BigDecimal sumOutstandingPayments(@Param("status") PaymentStatus status);
}
