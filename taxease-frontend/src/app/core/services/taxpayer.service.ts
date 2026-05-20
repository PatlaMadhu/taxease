import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { TaxpayerProfile, UpdateProfileRequest, TaxpayerDocument, DocumentUploadRequest } from '../models/taxpayer.model';

@Injectable({ providedIn: 'root' })
export class TaxpayerService {
  private base = `${environment.apiUrl}/taxpayers`;

  constructor(private http: HttpClient) {}

  getProfileByEmail(email: string): Observable<TaxpayerProfile> {
    return this.http.get<TaxpayerProfile>(`${this.base}/by-email/${email}`);
  }

  getProfile(): Observable<TaxpayerProfile> {
    return this.http.get<TaxpayerProfile>(`${this.base}/profile`);
  }

  getProfileById(id: number): Observable<TaxpayerProfile> {
    return this.http.get<TaxpayerProfile>(`${this.base}/${id}`);
  }

  updateProfile(payload: UpdateProfileRequest): Observable<TaxpayerProfile> {
    return this.http.put<TaxpayerProfile>(`${this.base}/profile`, payload);
  }

  getAllDocuments(): Observable<TaxpayerDocument[]> {
    return this.http.get<TaxpayerDocument[]>(`${this.base}/documents/all`);
  }

  verifyDocument(documentId: number, status: string): Observable<TaxpayerDocument> {
    return this.http.put<TaxpayerDocument>(`${this.base}/documents/${documentId}/verify`, {}, { params: { status } });
  }

  getDocuments(): Observable<TaxpayerDocument[]> {
    return this.http.get<TaxpayerDocument[]>(`${this.base}/documents`);
  }

  uploadDocument(payload: DocumentUploadRequest): Observable<TaxpayerDocument> {
    return this.http.post<TaxpayerDocument>(`${this.base}/documents`, payload);
  }

  updateDocument(documentId: number, payload: DocumentUploadRequest): Observable<TaxpayerDocument> {
    return this.http.put<TaxpayerDocument>(`${this.base}/documents/${documentId}`, payload);
  }

  deleteDocument(documentId: number): Observable<void> {
    return this.http.delete<void>(`${this.base}/documents/${documentId}`);
  }
}
