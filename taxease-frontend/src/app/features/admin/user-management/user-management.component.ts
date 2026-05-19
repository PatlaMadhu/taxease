import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { NotificationService } from '../../../core/services/notification.service';
import { ToastService } from '../../../core/services/toast.service';

@Component({
  standalone: false,
  selector: 'app-user-management',
  templateUrl: './user-management.component.html',
  styleUrls: ['./user-management.component.scss']
})
export class UserManagementComponent {
  broadcastForm: FormGroup;
  directForm: FormGroup;
  sending = false;
  categories = ['FILING', 'PAYMENT', 'COMPLIANCE', 'SYSTEM_UPDATE', 'BROADCAST'];

  constructor(private fb: FormBuilder, private notificationService: NotificationService, private toast: ToastService) {
    this.broadcastForm = this.fb.group({
      message: ['', Validators.required],
      category: ['SYSTEM_UPDATE', Validators.required]
    });
    this.directForm = this.fb.group({
      userId: ['', [Validators.required, Validators.min(1)]],
      message: ['', Validators.required],
      category: ['SYSTEM_UPDATE', Validators.required]
    });
  }

  sendBroadcast(): void {
    if (this.broadcastForm.invalid) return;
    this.sending = true;
    this.notificationService.broadcast(this.broadcastForm.value).subscribe({
      next: () => { this.toast.success('Broadcast sent!'); this.broadcastForm.reset({ category: 'SYSTEM_UPDATE' }); this.sending = false; },
      error: () => { this.sending = false; }
    });
  }

  sendDirect(): void {
    if (this.directForm.invalid) return;
    this.sending = true;
    const { userId, message, category } = this.directForm.value;
    this.notificationService.sendDirect(userId, message, category).subscribe({
      next: () => { this.toast.success(`Notification sent to user #${userId}!`); this.directForm.reset({ category: 'SYSTEM_UPDATE' }); this.sending = false; },
      error: () => { this.sending = false; }
    });
  }
}
