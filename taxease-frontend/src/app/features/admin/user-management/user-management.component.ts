import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { NotificationService } from '../../../core/services/notification.service';
import { TaxpayerService } from '../../../core/services/taxpayer.service';
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
  lookingUp = false;
  resolvedUserId: number | null = null;
  lookupError = '';
  categories = ['FILING', 'PAYMENT', 'COMPLIANCE', 'SYSTEM_UPDATE', 'BROADCAST', 'DEADLINE_ALERT', 'PROGRAM_UPDATE'];

  constructor(
    private fb: FormBuilder,
    private notificationService: NotificationService,
    private taxpayerService: TaxpayerService,
    private toast: ToastService
  ) {
    this.broadcastForm = this.fb.group({
      message: ['', Validators.required],
      category: ['SYSTEM_UPDATE', Validators.required]
    });
    this.directForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      message: ['', Validators.required],
      category: ['SYSTEM_UPDATE', Validators.required]
    });
  }

  lookupTaxpayer(): void {
    const email = this.directForm.get('email')?.value?.trim();
    if (!email) return;
    this.lookingUp = true;
    this.lookupError = '';
    this.resolvedUserId = null;
    this.taxpayerService.getProfileByEmail(email).subscribe({
      next: (profile) => {
        this.resolvedUserId = profile.userId;
        this.lookingUp = false;
      },
      error: () => {
        this.lookupError = 'No taxpayer found with that email.';
        this.lookingUp = false;
      }
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
    if (this.directForm.invalid || !this.resolvedUserId) return;
    this.sending = true;
    const { message, category } = this.directForm.value;
    this.notificationService.sendDirect(this.resolvedUserId, message, category).subscribe({
      next: () => {
        this.toast.success(`Notification sent to user #${this.resolvedUserId}!`);
        this.directForm.reset({ category: 'SYSTEM_UPDATE' });
        this.resolvedUserId = null;
        this.sending = false;
      },
      error: () => { this.sending = false; }
    });
  }
}
