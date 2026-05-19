package com.taxfilingservice.entity;

import com.taxfilingservice.entity.enums.FilingDocType;
import com.taxfilingservice.entity.enums.VerificationStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

@Entity
@Table(name = "filing_document")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class FilingDocument {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "document_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "filing_id", nullable = false)
    private TaxFiling taxFiling;

    @Enumerated(EnumType.STRING)
    @Column(name = "doc_type", nullable = false, length = 50)
    private FilingDocType docType;

    @Column(name = "file_uri", nullable = false, columnDefinition = "text")
    private String fileUri;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(name = "verification_status", nullable = false, length = 30)
    private VerificationStatus verificationStatus = VerificationStatus.PENDING;

    @CreationTimestamp
    @Column(name = "uploaded_date", nullable = false, updatable = false)
    private Instant uploadedDate;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;
}
