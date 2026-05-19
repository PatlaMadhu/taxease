import { Component, OnInit } from '@angular/core';
import { forkJoin, of } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { ReportService } from '../../../core/services/report.service';
import { ReportResponse } from '../../../core/models/report.model';
import { PaymentMetrics, RevenueDashboard } from '../../../core/models/payment.model';
import { AuditDashboard } from '../../../core/models/audit.model';

@Component({
  standalone: false,
  selector: 'app-report-list',
  templateUrl: './report-list.component.html',
  styleUrls: ['./report-list.component.scss']
})
export class ReportListComponent implements OnInit {
  reports: ReportResponse[] = [];
  metrics: PaymentMetrics | null = null;
  revenue: RevenueDashboard | null = null;
  auditDash: AuditDashboard | null = null;
  loading = true;

  barChartData: any = {
    labels: ['Successful', 'Failed', 'Total'],
    datasets: [{ label: 'Transactions', data: [0, 0, 0], backgroundColor: ['#2e7d32', '#c62828', '#1565c0'] }]
  };
  barChartOptions = { responsive: true, plugins: { legend: { display: false } } };

  lineChartData: any = {
    labels: ['Total Revenue', 'Successful', 'Pending'],
    datasets: [{ label: 'Revenue', data: [0, 0, 0], borderColor: '#1565c0', backgroundColor: 'rgba(21,101,192,0.1)', fill: true, tension: 0.4 }]
  };

  constructor(private reportService: ReportService) {}

  ngOnInit(): void {
    forkJoin({
      reports: this.reportService.getAllReports().pipe(catchError(() => of([]))),
      metrics: this.reportService.getPaymentMetrics().pipe(catchError(() => of(null))),
      revenue: this.reportService.getRevenueDashboard().pipe(catchError(() => of(null))),
      audit:   this.reportService.getAuditDashboard().pipe(catchError(() => of(null)))
    }).subscribe(({ reports, metrics, revenue, audit }) => {
      this.reports = reports;
      this.metrics = metrics;
      this.revenue = revenue;
      this.auditDash = audit;
      if (metrics) this.barChartData  = { ...this.barChartData,  datasets: [{ ...this.barChartData.datasets[0],  data: [metrics.successfulTransactions, metrics.failedTransactions, metrics.totalTransactions] }] };
      if (revenue) this.lineChartData = { ...this.lineChartData, datasets: [{ ...this.lineChartData.datasets[0], data: [revenue.totalRevenue, revenue.successfulRevenue, revenue.pendingRevenue] }] };
      this.loading = false;
    });
  }

  downloadCustom(): void {
    const today = new Date().toISOString().split('T')[0];
    this.reportService.downloadCustomReport('2024-01-01', today, 'REVENUE', ['totalRevenue', 'successfulRevenue']).subscribe(blob => {
      const url = URL.createObjectURL(blob);
      const a = document.createElement('a');
      a.href = url; a.download = 'report.csv'; a.click();
      URL.revokeObjectURL(url);
    });
  }
}
