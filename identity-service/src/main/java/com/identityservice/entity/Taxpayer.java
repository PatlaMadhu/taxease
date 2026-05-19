package com.identityservice.entity;

import com.identityservice.entity.entityEnum.TaxpayerType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

@Entity
@Table(name = "taxpayer")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Taxpayer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "taxpayer_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", unique = true, nullable = false)
    private User user;

    @Column(name = "name", length = 100)
    private String name;

    @Column(name = "taxpayer_id_number", unique = true, length = 50)
    private String taxpayerIdNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private TaxpayerType type;

    @Column(columnDefinition = "text")
    private String address;

    @Column(name = "contact_info", columnDefinition = "text")
    private String contactInfo;

    @CreationTimestamp
    private Instant createdAt;

    @UpdateTimestamp
    private Instant updatedAt;
}
