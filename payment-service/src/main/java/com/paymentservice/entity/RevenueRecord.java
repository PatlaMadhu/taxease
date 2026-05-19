package com.paymentservice.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "revenue_record",
        indexes = { @Index(name = "idx_revenue_taxpayer", columnList = "taxpayer_id") })
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class RevenueRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "revenue_id")
    private Long id;

    @Column(name = "payment_id", nullable = false)
    private Long paymentId;

    @Column(name = "taxpayer_id", nullable = false)
    private Long taxpayerId;

    @Column(name = "amount", nullable = false, precision = 15, scale = 2)
    private BigDecimal amount;

    @Column(name = "status", nullable = false, length = 30)
    private String status;

    @CreationTimestamp
    @Column(name = "record_date", nullable = false, updatable = false)
    private Instant recordDate;
}
