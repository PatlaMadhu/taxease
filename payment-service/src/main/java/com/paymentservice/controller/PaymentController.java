package com.paymentservice.controller;

import com.paymentservice.dto.PaymentMetricsResponse;
import com.paymentservice.dto.PaymentRequest;
import com.paymentservice.dto.PaymentResponse;
import com.paymentservice.dto.RevenueDashboardResponse;
import com.paymentservice.dto.RevenueResponse;
import com.paymentservice.entity.enums.PaymentMethod;
import com.paymentservice.entity.enums.PaymentStatus;
import com.paymentservice.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
@Slf4j
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/pay")
    public ResponseEntity<PaymentResponse> makePayment(@Valid @RequestBody PaymentRequest request) {
        log.info("Initiating payment for filing: {}", request.getFilingId());
        return ResponseEntity.status(HttpStatus.CREATED).body(paymentService.initiatePayment(request));
    }

    @GetMapping
    public ResponseEntity<List<PaymentResponse>> getAllPayments() {
        return ResponseEntity.ok(paymentService.getAllPayments());
    }

    @GetMapping("/{paymentId}")
    public ResponseEntity<PaymentResponse> getPaymentById(@PathVariable Long paymentId) {
        return ResponseEntity.ok(paymentService.getPaymentById(paymentId));
    }

    @GetMapping("/filing/{filingId}")
    public ResponseEntity<List<PaymentResponse>> getPaymentsByFiling(@PathVariable Long filingId) {
        return ResponseEntity.ok(paymentService.getPaymentsByFiling(filingId));
    }

    @GetMapping("/history/{taxpayerId}")
    public ResponseEntity<List<PaymentResponse>> getPaymentsByTaxpayer(@PathVariable Long taxpayerId) {
        log.info("Fetching payment history for taxpayer: {}", taxpayerId);
        return ResponseEntity.ok(paymentService.getPaymentsByTaxpayer(taxpayerId));
    }

    @PutMapping("/{paymentId}/status")
    public ResponseEntity<PaymentResponse> updateStatus(
            @PathVariable Long paymentId,
            @RequestParam PaymentStatus status) {
        log.info("Updating payment {} status to {}", paymentId, status);
        return ResponseEntity.ok(paymentService.updatePaymentStatus(paymentId, status));
    }

    @PostMapping("/retry/{oldPaymentId}")
    public ResponseEntity<PaymentResponse> retryPayment(
            @PathVariable Long oldPaymentId,
            @RequestParam PaymentMethod newMethod) {
        log.info("Retrying payment: {} with method: {}", oldPaymentId, newMethod);
        return ResponseEntity.ok(paymentService.retryPayment(oldPaymentId, newMethod));
    }

    @GetMapping("/metrics")
    public ResponseEntity<PaymentMetricsResponse> getPaymentMetrics() {
        log.info("Fetching payment metrics");
        return ResponseEntity.ok(paymentService.getPaymentMetrics());
    }

    @GetMapping("/revenue")
    public ResponseEntity<RevenueDashboardResponse> getRevenueDashboard() {
        log.info("Fetching revenue dashboard");
        return ResponseEntity.ok(paymentService.getRevenueDashboard());
    }

    @GetMapping("/revenue/taxpayer/{taxpayerId}")
    public ResponseEntity<List<RevenueResponse>> getRevenueByTaxpayer(@PathVariable Long taxpayerId) {
        return ResponseEntity.ok(paymentService.getRevenueByTaxpayer(taxpayerId));
    }
}
