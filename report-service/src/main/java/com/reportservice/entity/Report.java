package com.reportservice.entity;

import com.reportservice.entity.enums.ReportScope;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

@Entity
@Table(name = "report",
        indexes = { @Index(name = "idx_report_scope", columnList = "scope") })
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "report_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "scope", nullable = false, length = 30)
    private ReportScope scope;

    @Column(name = "title", nullable = false, length = 200)
    private String title;

    @Lob
    @Column(name = "metrics", nullable = false)
    private String metrics;

    @Column(name = "generated_by")
    private Long generatedBy;

    @CreationTimestamp
    @Column(name = "generated_date", nullable = false, updatable = false)
    private Instant generatedDate;
}
