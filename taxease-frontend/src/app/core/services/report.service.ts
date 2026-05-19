import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { ReportRequest, ReportResponse, ReportScope } from '../models/report.model';
import { PaymentMetrics, RevenueDashboard } from '../models/payment.model';
import { AuditDashboard } from '../models/audit.model';

@Injectable({ providedIn: 'root' })
export class ReportService {
  private base = `${environment.apiUrl}/reports`;

  constructor(private http: HttpClient) {}

  generateReport(payload: ReportRequest): Observable<ReportResponse> {
    return this.http.post<ReportResponse>(this.base, payload);
  }

  getAllReports(): Observable<ReportResponse[]> {
    return this.http.get<ReportResponse[]>(this.base);
  }

  getReportById(id: number): Observable<ReportResponse> {
    return this.http.get<ReportResponse>(`${this.base}/${id}`);
  }

  getByScope(scope: ReportScope): Observable<ReportResponse[]> {
    return this.http.get<ReportResponse[]>(`${this.base}/scope/${scope}`);
  }

  getPaymentMetrics(): Observable<PaymentMetrics> {
    return this.http.get<PaymentMetrics>(`${this.base}/payments/metrics`);
  }

  getAuditDashboard(): Observable<AuditDashboard> {
    return this.http.get<AuditDashboard>(`${this.base}/audits/dashboard`);
  }

  getRevenueDashboard(period?: string, taxpayerType?: string): Observable<RevenueDashboard> {
    let params = new HttpParams();
    if (period) params = params.set('period', period);
    if (taxpayerType) params = params.set('taxpayerType', taxpayerType);
    return this.http.get<RevenueDashboard>(`${this.base}/revenue/dashboard`, { params });
  }

  downloadCustomReport(startDate: string, endDate: string, reportType: string, metrics: string[]): Observable<Blob> {
    let params = new HttpParams()
      .set('startDate', startDate)
      .set('endDate', endDate)
      .set('reportType', reportType);
    metrics.forEach(m => params = params.append('metrics', m));
    return this.http.get(`${this.base}/custom/download`, { params, responseType: 'blob' });
  }
}
