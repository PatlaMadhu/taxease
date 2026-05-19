export type PaymentMethod = 'Bank' | 'Wallet';
export type PaymentStatus = 'Pending' | 'Completed' | 'Failed';

export interface PaymentRequest {
  filingId: number;
  taxpayerId: number;
  amount: number;
  method: PaymentMethod;
}

export interface PaymentResponse {
  paymentId: number;
  filingId: number;
  taxpayerId: number;
  amount: number;
  method: PaymentMethod;
  status: PaymentStatus;
  paymentDate: string;
}

export interface PaymentMetrics {
  successfulTransactions: number;
  failedTransactions: number;
  totalTransactions: number;
}

export interface RevenueDashboard {
  totalRevenue: number;
  successfulRevenue: number;
  pendingRevenue: number;
  totalTransactions: number;
}

export interface RevenueRecord {
  revenueId: number;
  paymentId: number;
  taxpayerId: number;
  amount: number;
  status: string;
  recordDate: string;
}
