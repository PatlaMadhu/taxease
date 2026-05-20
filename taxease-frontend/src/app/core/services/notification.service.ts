import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { NotificationResponse, NotificationRequest, BroadcastRequest } from '../models/notification.model';

@Injectable({ providedIn: 'root' })
export class NotificationService {
  private base = `${environment.apiUrl}/notifications`;

  constructor(private http: HttpClient) {}

  send(payload: NotificationRequest): Observable<NotificationResponse> {
    return this.http.post<NotificationResponse>(this.base, payload);
  }

  broadcast(payload: BroadcastRequest): Observable<string> {
    return this.http.post(`${this.base}/broadcast`, payload, { responseType: 'text' });
  }

  sendDirect(userId: number, message: string, category: string): Observable<string> {
    return this.http.post(`${this.base}/user/${userId}`, { message, category }, { responseType: 'text' });
  }

  getByUser(userId: number): Observable<NotificationResponse[]> {
    return this.http.get<NotificationResponse[]>(`${this.base}/my`);
  }

  getUnread(userId: number): Observable<NotificationResponse[]> {
    return this.http.get<NotificationResponse[]>(`${this.base}/my/unread`);
  }

  markAsRead(notificationId: number, userId: number): Observable<string> {
    return this.http.put(`${this.base}/${notificationId}/read`, {}, { responseType: 'text' });
  }
}
