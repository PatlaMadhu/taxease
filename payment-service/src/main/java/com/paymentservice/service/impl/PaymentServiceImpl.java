package com.paymentservice.service.impl;

import com.paymentservice.dto.PaymentMetricsResponse;
import com.paymentservice.dto.PaymentRequest;
import com.paymentservice.dto.PaymentResponse;
import com.paymentservice.dto.RevenueDashboardResponse;
import com.paymentservice.dto.RevenueResponse;
import com.paymentservice.entity.Payment;
import com.paymentservice.entity.RevenueRecord;
import com.paymentservice.entity.enums.PaymentMethod;
import com.paymentservice.entity.enums.PaymentStatus;
import com.paymentservice.event.PaymentCompletedEvent;
import com.paymentservice.event.PaymentEventPublisher;
import com.paymentservice.repository.PaymentRepository;
import com.paymentservice.repository.RevenueRecordRepository;
import com.paymentservice.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final RevenueRecordRepository revenueRecordRepository;
    private final PaymentEventPublisher eventPublisher;

    @Override
    public List<PaymentResponse> getAllPayments() {
        return paymentRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Override
    @Transactional
    public PaymentResponse initiatePayment(PaymentRequest request) {
        log.info("Initiating payment for filingId: {}", request.getFilingId());
        Payment payment = Payment.builder()
                .filingId(request.getFilingId())
                .taxpayerId(request.getTaxpayerId())
                .amount(request.getAmount())
                .method(request.getMethod())
                .status(PaymentStatus.Pending)
                .build();
        Payment saved = paymentRepository.save(payment);

        RevenueRecord record = RevenueRecord.builder()
                .paymentId(saved.getId())
                .taxpayerId(saved.getTaxpayerId())
                .amount(saved.getAmount())
                .status("PENDING")
                .build();
        revenueRecordRepository.save(record);

        return toResponse(saved);
    }

    @Override
    public PaymentResponse getPaymentById(Long paymentId) {
        return toResponse(findPayment(paymentId));
    }

    @Override
    public List<PaymentResponse> getPaymentsByFiling(Long filingId) {
        return paymentRepository.findByFilingId(filingId).stream().map(this::toResponse).toList();
    }

    @Override
    public List<PaymentResponse> getPaymentsByTaxpayer(Long taxpayerId) {
        return paymentRepository.findByTaxpayerId(taxpayerId).stream().map(this::toResponse).toList();
    }

    @Override
    @Transactional
    public PaymentResponse updatePaymentStatus(Long paymentId, PaymentStatus status) {
        Payment payment = findPayment(paymentId);
        payment.setStatus(status);
        Payment saved = paymentRepository.save(payment);

        revenueRecordRepository.findByTaxpayerId(payment.getTaxpayerId()).stream()
                .filter(r -> r.getPaymentId().equals(paymentId))
                .findFirst()
                .ifPresent(r -> {
                    r.setStatus(status.name());
                    revenueRecordRepository.save(r);
                });

        if (status == PaymentStatus.Completed) {
            eventPublisher.publishPaymentCompleted(PaymentCompletedEvent.builder()
                    .paymentId(saved.getId())
                    .filingId(saved.getFilingId())
                    .taxpayerId(saved.getTaxpayerId())
                    .amount(saved.getAmount())
                    .status(saved.getStatus().name())
                    .build());
        }

        return toResponse(saved);
    }

    @Override
    public List<RevenueResponse> getRevenueByTaxpayer(Long taxpayerId) {
        return revenueRecordRepository.findByTaxpayerId(taxpayerId).stream()
                .map(r -> RevenueResponse.builder()
                        .revenueId(r.getId())
                        .paymentId(r.getPaymentId())
                        .taxpayerId(r.getTaxpayerId())
                        .amount(r.getAmount())
                        .status(r.getStatus())
                        .recordDate(r.getRecordDate())
                        .build())
                .toList();
    }

    @Override
    @Transactional
    public PaymentResponse retryPayment(Long oldPaymentId, PaymentMethod newMethod) {
        Payment oldPayment = findPayment(oldPaymentId);
        if (oldPayment.getStatus() != PaymentStatus.Failed) {
            throw new IllegalStateException("Only failed payments can be retried");
        }
        PaymentRequest retryRequest = PaymentRequest.builder()
                .filingId(oldPayment.getFilingId())
                .taxpayerId(oldPayment.getTaxpayerId())
                .amount(oldPayment.getAmount())
                .method(newMethod)
                .build();
        return initiatePayment(retryRequest);
    }

    @Override
    public PaymentMetricsResponse getPaymentMetrics() {
        long successful = paymentRepository.countByStatus(PaymentStatus.Completed);
        long failed = paymentRepository.countByStatus(PaymentStatus.Failed);
        long total = paymentRepository.count();
        return PaymentMetricsResponse.builder()
                .successfulTransactions(successful)
                .failedTransactions(failed)
                .totalTransactions(total)
                .build();
    }

    @Override
    public RevenueDashboardResponse getRevenueDashboard() {
        BigDecimal collected = revenueRecordRepository.sumCollectedRevenue(PaymentStatus.Completed.name());
        BigDecimal outstanding = paymentRepository.sumOutstandingPayments(PaymentStatus.Pending);
        long total = paymentRepository.count();
        return RevenueDashboardResponse.builder()
                .totalRevenue(collected != null ? collected : BigDecimal.ZERO)
                .successfulRevenue(collected != null ? collected : BigDecimal.ZERO)
                .pendingRevenue(outstanding != null ? outstanding : BigDecimal.ZERO)
                .totalTransactions(total)
                .build();
    }

    private Payment findPayment(Long id) {
        return paymentRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Payment not found: " + id));
    }

    private PaymentResponse toResponse(Payment p) {
        return PaymentResponse.builder()
                .paymentId(p.getId())
                .filingId(p.getFilingId())
                .taxpayerId(p.getTaxpayerId())
                .amount(p.getAmount())
                .method(p.getMethod())
                .status(p.getStatus())
                .paymentDate(p.getPaymentDate())
                .build();
    }
}
