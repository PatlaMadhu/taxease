package com.taxpayerservice.entity;

import com.taxpayerservice.entity.enums.TaxpayerType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "taxpayer",
        indexes = {
                @Index(name = "idx_taxpayer_user", columnList = "user_id", unique = true),
                @Index(name = "idx_taxpayer_id_number", columnList = "taxpayer_id_number", unique = true)
        })
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Taxpayer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "taxpayer_id")
    private Long id;

    // Reference to identity-service User — stored as plain ID (no FK across services)
    @Column(name = "user_id", nullable = false, unique = true)
    private Long userId;

    @Column(name = "name", length = 100)
    private String name;

    @Column(name = "email", nullable = false, unique = true, length = 255)
    private String email;

    @Column(name = "phone", length = 30)
    private String phone;

    @Column(name = "taxpayer_id_number", unique = true, length = 50)
    private String taxpayerIdNumber;

    @Column(name = "pan_number", length = 20)
    private String panNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", length = 30)
    private TaxpayerType type;

    @Column(columnDefinition = "text")
    private String address;

    @Column(name = "contact_info", columnDefinition = "text")
    private String contactInfo;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @OneToMany(mappedBy = "taxpayer", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<TaxpayerDocument> documents = new ArrayList<>();
}
