import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable, of } from 'rxjs';
import { shareReplay, tap } from 'rxjs/operators';
import { environment } from '../../../environments/environment';
import { FilingRequest, FilingResponse, FilingDocument, DocumentUploadRequest, FilingStatus } from '../models/filing.model';

@Injectable({ providedIn: 'root' })
export class FilingService {
  private base = `${environment.apiUrl}/filings`;
  private cache = new Map<string, Observable<any>>();

  constructor(private http: HttpClient) {}

  private cached<T>(key: string, req: Observable<T>): Observable<T> {
    if (!this.cache.has(key)) this.cache.set(key, req.pipe(shareReplay(1)));
    return this.cache.get(key)!;
  }

  invalidate(key?: string): void {
    key ? this.cache.delete(key) : this.cache.clear();
  }

  createFiling(payload: FilingRequest): Observable<FilingResponse> {
    return this.http.post<FilingResponse>(this.base, payload).pipe(
      tap(() => this.cache.clear())
    );
  }

  getFilingById(id: number): Observable<FilingResponse> {
    return this.cached(`filing-${id}`, this.http.get<FilingResponse>(`${this.base}/${id}`));
  }

  getAllFilings(): Observable<FilingResponse[]> {
    return this.cached('all-filings', this.http.get<FilingResponse[]>(this.base));
  }

  getFilingsByTaxpayer(taxpayerId: number): Observable<FilingResponse[]> {
    return this.cached(`filings-tp-${taxpayerId}`, this.http.get<FilingResponse[]>(`${this.base}/taxpayer/${taxpayerId}`));
  }

  getFilingsByStatus(status: FilingStatus): Observable<FilingResponse[]> {
    return this.cached(`filings-status-${status}`, this.http.get<FilingResponse[]>(`${this.base}/status/${status}`));
  }

  submitFiling(filingId: number): Observable<FilingResponse> {
    return this.http.put<FilingResponse>(`${this.base}/${filingId}/submit`, {}).pipe(
      tap(() => this.cache.clear())
    );
  }

  updateFilingStatus(filingId: number, status: FilingStatus): Observable<FilingResponse> {
    const params = new HttpParams().set('status', status);
    return this.http.put<FilingResponse>(`${this.base}/${filingId}/status`, {}, { params }).pipe(
      tap(() => this.cache.clear())
    );
  }

  uploadDocument(filingId: number, payload: DocumentUploadRequest): Observable<FilingDocument> {
    return this.http.post<FilingDocument>(`${this.base}/${filingId}/documents`, payload);
  }

  getDocuments(filingId: number): Observable<FilingDocument[]> {
    return this.cached(`docs-${filingId}`, this.http.get<FilingDocument[]>(`${this.base}/${filingId}/documents`));
  }
}
