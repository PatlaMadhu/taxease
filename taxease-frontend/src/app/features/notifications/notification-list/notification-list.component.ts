import { Component, OnInit } from '@angular/core';
import { of } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { NotificationService } from '../../../core/services/notification.service';
import { AuthService } from '../../../core/services/auth.service';
import { NotificationResponse } from '../../../core/models/notification.model';
import { ToastService } from '../../../core/services/toast.service';

@Component({
  standalone: false,
  selector: 'app-notification-list',
  templateUrl: './notification-list.component.html',
  styleUrls: ['./notification-list.component.scss']
})
export class NotificationListComponent implements OnInit {
  notifications: NotificationResponse[] = [];
  loading = true;
  error = false;
  userId!: number;

  constructor(
    private notificationService: NotificationService,
    public auth: AuthService,
    private toast: ToastService
  ) {}

  ngOnInit(): void {
    const userId = this.auth.getUserId();
    if (!userId) { this.loading = false; return; }
    this.userId = userId;
    this.load();
  }

  load(): void {
    this.loading = true;
    this.error = false;
    this.notificationService.getByUser(this.userId)
      .pipe(catchError(err => {
        console.error('Notification load error:', err);
        this.error = true;
        this.loading = false;
        return of([]);
      }))
      .subscribe(data => {
        this.notifications = data;
        this.loading = false;
      });
  }

  markRead(notification: NotificationResponse): void {
    if (notification.status === 'READ') return;
    this.notificationService.markAsRead(notification.notificationId, this.userId)
      .pipe(catchError(() => of('')))
      .subscribe(() => {
        notification.status = 'READ';
        this.toast.info('Notification marked as read.');
      });
  }

  get unreadCount(): number { return this.notifications.filter(n => n.status === 'UNREAD').length; }
}
