package com.paymentservice.entity;

import com.paymentservice.entity.enums.PaymentMethod;
import com.paymentservice.entity.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "payment",
        indexes = {
                @Index(name = "idx_payment_filing", columnList = "filing_id"),
                @Index(name = "idx_payment_status", columnList = "status")
        })
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id")
    private Long id;

    @Column(name = "filing_id", nullable = false)
    private Long filingId;

    @Column(name = "taxpayer_id", nullable = false)
    private Long taxpayerId;

    @Column(name = "amount", nullable = false, precision = 15, scale = 2)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "method", nullable = false, length = 20)
    private PaymentMethod method;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private PaymentStatus status = PaymentStatus.Pending;

    @CreationTimestamp
    @Column(name = "payment_date", nullable = false, updatable = false)
    private Instant paymentDate;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;
}
