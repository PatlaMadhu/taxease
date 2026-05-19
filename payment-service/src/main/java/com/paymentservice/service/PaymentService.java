package com.paymentservice.service;

import com.paymentservice.dto.PaymentMetricsResponse;
import com.paymentservice.dto.PaymentRequest;
import com.paymentservice.dto.PaymentResponse;
import com.paymentservice.dto.RevenueDashboardResponse;
import com.paymentservice.dto.RevenueResponse;
import com.paymentservice.entity.enums.PaymentMethod;
import com.paymentservice.entity.enums.PaymentStatus;

import java.util.List;

public interface PaymentService {
    List<PaymentResponse> getAllPayments();
    PaymentResponse initiatePayment(PaymentRequest request);
    PaymentResponse getPaymentById(Long paymentId);
    List<PaymentResponse> getPaymentsByFiling(Long filingId);
    List<PaymentResponse> getPaymentsByTaxpayer(Long taxpayerId);
    PaymentResponse updatePaymentStatus(Long paymentId, PaymentStatus status);
    PaymentResponse retryPayment(Long oldPaymentId, PaymentMethod newMethod);
    PaymentMetricsResponse getPaymentMetrics();
    RevenueDashboardResponse getRevenueDashboard();
    List<RevenueResponse> getRevenueByTaxpayer(Long taxpayerId);
}
