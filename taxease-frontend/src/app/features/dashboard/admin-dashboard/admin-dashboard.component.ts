import { Component, OnInit, ChangeDetectionStrategy, ChangeDetectorRef } from '@angular/core';
import { forkJoin, of } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { PaymentService } from '../../../core/services/payment.service';
import { AuditService } from '../../../core/services/audit.service';
import { PaymentMetrics, RevenueDashboard } from '../../../core/models/payment.model';
import { AuditDashboard } from '../../../core/models/audit.model';

@Component({
  standalone: false,
  selector: 'app-admin-dashboard',
  templateUrl: './admin-dashboard.component.html',
  styleUrls: ['./admin-dashboard.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class AdminDashboardComponent implements OnInit {
  metrics: PaymentMetrics | null = null;
  revenue: RevenueDashboard | null = null;
  auditDash: AuditDashboard | null = null;
  loading = true;

  revenueChartData: any = {
    labels: ['Total', 'Successful', 'Pending'],
    datasets: [{ data: [0, 0, 0], backgroundColor: ['#1565c0', '#2e7d32', '#e65100'] }]
  };
  auditChartData: any = {
    labels: ['Open', 'Closed', 'Non-Compliance'],
    datasets: [{ data: [0, 0, 0], backgroundColor: ['#e65100', '#2e7d32', '#c62828'] }]
  };

  constructor(
    private paymentService: PaymentService,
    private auditService: AuditService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    forkJoin({
      metrics: this.paymentService.getPaymentMetrics().pipe(catchError(() => of(null))),
      revenue: this.paymentService.getRevenueDashboard().pipe(catchError(() => of(null))),
      audit:   this.auditService.getAuditDashboard().pipe(catchError(() => of(null)))
    }).subscribe(({ metrics, revenue, audit }) => {
      this.metrics = metrics;
      this.revenue = revenue;
      this.auditDash = audit;
      if (revenue) this.revenueChartData = { ...this.revenueChartData, datasets: [{ ...this.revenueChartData.datasets[0], data: [revenue.totalRevenue, revenue.successfulRevenue, revenue.pendingRevenue] }] };
      if (audit)   this.auditChartData   = { ...this.auditChartData,   datasets: [{ ...this.auditChartData.datasets[0],   data: [audit.openAudits, audit.closedAudits, audit.nonComplianceFilings] }] };
      this.loading = false;
      this.cdr.markForCheck();
    });
  }
}
