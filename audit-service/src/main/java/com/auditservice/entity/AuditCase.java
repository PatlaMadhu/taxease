package com.auditservice.entity;

import com.auditservice.entity.enums.AuditStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

@Entity
@Table(name = "audit_case",
        indexes = { @Index(name = "idx_audit_officer", columnList = "officer_id") })
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class AuditCase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "audit_id")
    private Long id;

    @Column(name = "officer_id", nullable = false)
    private Long officerId;

    @Column(name = "taxpayer_id")
    private Long taxpayerId;

    @Column(name = "scope", length = 200)
    private String scope;

    @Lob
    @Column(name = "findings")
    private String findings;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 30)
    private AuditStatus status = AuditStatus.OPEN;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;
}
