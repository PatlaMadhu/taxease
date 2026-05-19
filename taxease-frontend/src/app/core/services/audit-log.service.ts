import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { AuditLogResponse } from '../models/audit-log.model';

@Injectable({ providedIn: 'root' })
export class AuditLogService {
  private base = `${environment.apiUrl}/audit-logs`;

  constructor(private http: HttpClient) {}

  getAll(): Observable<AuditLogResponse[]> {
    return this.http.get<AuditLogResponse[]>(this.base);
  }

  getById(id: number): Observable<AuditLogResponse> {
    return this.http.get<AuditLogResponse>(`${this.base}/${id}`);
  }
}
