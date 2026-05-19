package com.auditservice.entity;

import com.auditservice.entity.enums.ComplianceType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

@Entity
@Table(name = "compliance_record",
        indexes = { @Index(name = "idx_compliance_entity", columnList = "entity_id") })
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ComplianceRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "compliance_id")
    private Long id;

    @Column(name = "entity_id", nullable = false)
    private Long entityId;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, length = 30)
    private ComplianceType type;

    @Column(name = "result", nullable = false, length = 50)
    private String result;

    @Column(name = "notes", columnDefinition = "text")
    private String notes;

    @CreationTimestamp
    @Column(name = "record_date", nullable = false, updatable = false)
    private Instant recordDate;
}
