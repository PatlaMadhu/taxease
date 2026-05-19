import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { shareReplay, tap } from 'rxjs/operators';
import { environment } from '../../../environments/environment';
import { AuditRequest, AuditResponse, ComplianceRequest, ComplianceResponse, AuditStatus, AuditDashboard } from '../models/audit.model';

@Injectable({ providedIn: 'root' })
export class AuditService {
  private base = `${environment.apiUrl}/audits`;
  private cache = new Map<string, Observable<any>>();

  constructor(private http: HttpClient) {}

  private cached<T>(key: string, req: Observable<T>): Observable<T> {
    if (!this.cache.has(key)) this.cache.set(key, req.pipe(shareReplay(1)));
    return this.cache.get(key)!;
  }

  invalidate(): void { this.cache.clear(); }

  createAudit(payload: AuditRequest): Observable<AuditResponse> {
    return this.http.post<AuditResponse>(this.base, payload).pipe(tap(() => this.cache.clear()));
  }

  getAllAudits(): Observable<AuditResponse[]> {
    return this.cached('all-audits', this.http.get<AuditResponse[]>(this.base));
  }

  getAuditById(id: number): Observable<AuditResponse> {
    return this.cached(`audit-${id}`, this.http.get<AuditResponse>(`${this.base}/${id}`));
  }

  getAuditsByOfficer(officerId: number): Observable<AuditResponse[]> {
    return this.cached(`audits-officer-${officerId}`, this.http.get<AuditResponse[]>(`${this.base}/officer/${officerId}`));
  }

  closeAudit(auditId: number, findings: string): Observable<AuditResponse> {
    const params = new HttpParams().set('findings', findings);
    return this.http.put<AuditResponse>(`${this.base}/${auditId}/close`, {}, { params }).pipe(tap(() => this.cache.clear()));
  }

  updateAuditStatus(auditId: number, status: AuditStatus): Observable<AuditResponse> {
    const params = new HttpParams().set('status', status);
    return this.http.put<AuditResponse>(`${this.base}/${auditId}/status`, {}, { params }).pipe(tap(() => this.cache.clear()));
  }

  createCompliance(payload: ComplianceRequest): Observable<ComplianceResponse> {
    return this.http.post<ComplianceResponse>(`${this.base}/compliance`, payload).pipe(tap(() => this.cache.clear()));
  }

  getAllCompliance(): Observable<ComplianceResponse[]> {
    return this.cached('all-compliance', this.http.get<ComplianceResponse[]>(`${this.base}/compliance`));
  }

  getComplianceByEntity(entityId: number): Observable<ComplianceResponse[]> {
    return this.cached(`compliance-entity-${entityId}`, this.http.get<ComplianceResponse[]>(`${this.base}/compliance/entity/${entityId}`));
  }

  getAuditDashboard(): Observable<AuditDashboard> {
    return this.cached('audit-dashboard', this.http.get<AuditDashboard>(`${this.base}/dashboard`));
  }
}
