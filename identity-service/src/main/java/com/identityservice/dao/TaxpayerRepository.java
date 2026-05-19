package com.identityservice.dao;

import com.identityservice.entity.Taxpayer;
import com.identityservice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface TaxpayerRepository extends JpaRepository<Taxpayer, Long> {
    boolean existsByTaxpayerIdNumber(String taxpayerIdNumber);
    Optional<Taxpayer> findByUser(User user);
}