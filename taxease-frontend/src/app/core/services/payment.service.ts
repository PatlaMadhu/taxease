import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { shareReplay, tap } from 'rxjs/operators';
import { environment } from '../../../environments/environment';
import { PaymentRequest, PaymentResponse, PaymentMetrics, RevenueDashboard, RevenueRecord, PaymentMethod, PaymentStatus } from '../models/payment.model';

@Injectable({ providedIn: 'root' })
export class PaymentService {
  private base = `${environment.apiUrl}/payments`;
  private cache = new Map<string, Observable<any>>();

  constructor(private http: HttpClient) {}

  private cached<T>(key: string, req: Observable<T>): Observable<T> {
    if (!this.cache.has(key)) this.cache.set(key, req.pipe(shareReplay(1)));
    return this.cache.get(key)!;
  }

  invalidate(): void { this.cache.clear(); }

  makePayment(payload: PaymentRequest): Observable<PaymentResponse> {
    return this.http.post<PaymentResponse>(`${this.base}/pay`, payload).pipe(
      tap(() => this.cache.clear())
    );
  }

  getPaymentById(id: number): Observable<PaymentResponse> {
    return this.cached(`payment-${id}`, this.http.get<PaymentResponse>(`${this.base}/${id}`));
  }

  getPaymentsByFiling(filingId: number): Observable<PaymentResponse[]> {
    return this.cached(`payments-filing-${filingId}`, this.http.get<PaymentResponse[]>(`${this.base}/filing/${filingId}`));
  }

  getPaymentHistory(taxpayerId: number): Observable<PaymentResponse[]> {
    return this.cached(`history-${taxpayerId}`, this.http.get<PaymentResponse[]>(`${this.base}/history/${taxpayerId}`));
  }

  getAllPayments(): Observable<PaymentResponse[]> {
    return this.cached('all-payments', this.http.get<PaymentResponse[]>(this.base));
  }

  updatePaymentStatus(paymentId: number, status: PaymentStatus): Observable<PaymentResponse> {
    const params = new HttpParams().set('status', status);
    return this.http.put<PaymentResponse>(`${this.base}/${paymentId}/status`, {}, { params }).pipe(
      tap(() => this.cache.clear())
    );
  }

  retryPayment(oldPaymentId: number, newMethod: PaymentMethod): Observable<PaymentResponse> {
    const params = new HttpParams().set('newMethod', newMethod);
    return this.http.post<PaymentResponse>(`${this.base}/retry/${oldPaymentId}`, {}, { params }).pipe(
      tap(() => this.cache.clear())
    );
  }

  getPaymentMetrics(): Observable<PaymentMetrics> {
    return this.cached('metrics', this.http.get<PaymentMetrics>(`${this.base}/metrics`));
  }

  getRevenueDashboard(): Observable<RevenueDashboard> {
    return this.cached('revenue', this.http.get<RevenueDashboard>(`${this.base}/revenue`));
  }

  getRevenueByTaxpayer(taxpayerId: number): Observable<RevenueRecord[]> {
    return this.cached(`revenue-tp-${taxpayerId}`, this.http.get<RevenueRecord[]>(`${this.base}/revenue/taxpayer/${taxpayerId}`));
  }
}
