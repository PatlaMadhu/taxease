package com.taxfilingservice;

import com.taxfilingservice.repository.TaxFilingRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
@EnableTransactionManagement
@EnableAsync
@Slf4j
public class TaxFilingServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(TaxFilingServiceApplication.class, args);
    }

    @Bean
    CommandLineRunner migrateDrafts(TaxFilingRepository repo) {
        return args -> {
            int updated = repo.migrateAllDraftsToSubmitted();
            if (updated > 0) log.info("Migrated {} DRAFT filing(s) to SUBMITTED", updated);
        };
    }
}
