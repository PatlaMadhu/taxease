package com.taxpayerservice.repository;

import com.taxpayerservice.entity.Taxpayer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TaxpayerRepository extends JpaRepository<Taxpayer, Long> {
    Optional<Taxpayer> findByEmail(String email);
    Optional<Taxpayer> findByUserId(Long userId);
    boolean existsByEmail(String email);
}
