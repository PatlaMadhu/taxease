package com.taxfilingservice.entity;

import com.taxfilingservice.entity.enums.FilingStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tax_filing",
        indexes = {
                @Index(name = "idx_filing_taxpayer", columnList = "taxpayer_id"),
                @Index(name = "idx_filing_status", columnList = "status")
        })
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class TaxFiling {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "filing_id")
    private Long id;

    @Column(name = "taxpayer_id", nullable = false)
    private Long taxpayerId;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "taxpayer_email", nullable = false, length = 255)
    private String taxpayerEmail;

    @Column(name = "period", nullable = false, length = 20)
    private String period;

    @Column(name = "amount_declared", nullable = false, precision = 15, scale = 2)
    private BigDecimal amountDeclared;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 30)
    @Builder.Default
    private FilingStatus status = FilingStatus.DRAFT;

    @CreationTimestamp
    @Column(name = "submitted_date", nullable = false, updatable = false)
    private Instant submittedDate;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @OneToMany(mappedBy = "taxFiling", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<FilingDocument> documents = new ArrayList<>();
}
