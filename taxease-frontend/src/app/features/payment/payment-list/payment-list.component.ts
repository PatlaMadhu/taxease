import { Component, OnInit, ViewChild, AfterViewInit } from '@angular/core';
import { MatTableDataSource } from '@angular/material/table';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { PaymentService } from '../../../core/services/payment.service';
import { ProfileCacheService } from '../../../core/services/profile-cache.service';
import { AuthService } from '../../../core/services/auth.service';
import { PaymentResponse, PaymentStatus } from '../../../core/models/payment.model';
import { ToastService } from '../../../core/services/toast.service';

@Component({
  standalone: false,
  selector: 'app-payment-list',
  templateUrl: './payment-list.component.html',
  styleUrls: ['./payment-list.component.scss']
})
export class PaymentListComponent implements OnInit, AfterViewInit {
  dataSource = new MatTableDataSource<PaymentResponse>();
  displayedColumns = ['paymentId', 'filingId', 'amount', 'method', 'status', 'paymentDate', 'actions'];
  loading = true;
  error = false;
  isOfficer = false;

  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;

  constructor(
    private paymentService: PaymentService,
    private profileCache: ProfileCacheService,
    public auth: AuthService,
    private toast: ToastService
  ) {}

  ngAfterViewInit(): void {
    this.dataSource.paginator = this.paginator;
    this.dataSource.sort = this.sort;
  }

  ngOnInit(): void {
    const isOfficer = this.auth.hasRole('OFFICER', 'ADMINISTRATOR', 'MANAGER', 'COMPLIANCE', 'AUDITOR');
    this.isOfficer = isOfficer;
    if (isOfficer) {
      this.paymentService.getAllPayments().subscribe({
        next: data => { this.dataSource.data = data; this.loading = false; },
        error: () => { this.loading = false; this.error = true; }
      });
    } else {
      this.profileCache.getProfile().subscribe({
        next: profile => {
          if (!profile) { this.loading = false; return; }
          this.paymentService.getPaymentHistory(profile.taxpayerId).subscribe({
            next: data => { this.dataSource.data = data; this.loading = false; },
            error: () => { this.loading = false; this.error = true; }
          });
        },
        error: () => { this.loading = false; this.error = true; }
      });
    }
  }

  applyFilter(event: Event): void {
    this.dataSource.filter = (event.target as HTMLInputElement).value.trim().toLowerCase();
  }

  updateStatus(payment: PaymentResponse, status: PaymentStatus): void {
    this.paymentService.updatePaymentStatus(payment.paymentId, status).subscribe({
      next: updated => {
        payment.status = updated.status;
        this.toast.success(`Payment #${payment.paymentId} marked as ${status}`);
      },
      error: () => this.toast.error('Failed to update payment status.')
    });
  }
}
