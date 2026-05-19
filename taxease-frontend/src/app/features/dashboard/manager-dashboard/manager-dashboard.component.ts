import { Component, OnInit } from '@angular/core';
import { forkJoin, of } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { PaymentService } from '../../../core/services/payment.service';
import { FilingService } from '../../../core/services/filing.service';
import { PaymentMetrics, RevenueDashboard } from '../../../core/models/payment.model';
import { FilingResponse } from '../../../core/models/filing.model';

@Component({
  standalone: false,
  selector: 'app-manager-dashboard',
  templateUrl: './manager-dashboard.component.html',
  styleUrls: ['./manager-dashboard.component.scss']
})
export class ManagerDashboardComponent implements OnInit {
  metrics: PaymentMetrics | null = null;
  revenue: RevenueDashboard | null = null;
  recentFilings: FilingResponse[] = [];
  loading = true;

  revenueChartData: any = {
    labels: ['Total', 'Successful', 'Pending'],
    datasets: [{ data: [0, 0, 0], backgroundColor: ['#1565c0', '#2e7d32', '#e65100'] }]
  };

  constructor(private paymentService: PaymentService, private filingService: FilingService) {}

  ngOnInit(): void {
    forkJoin({
      metrics: this.paymentService.getPaymentMetrics().pipe(catchError(() => of(null))),
      revenue: this.paymentService.getRevenueDashboard().pipe(catchError(() => of(null))),
      filings: this.filingService.getAllFilings().pipe(catchError(() => of([])))
    }).subscribe(({ metrics, revenue, filings }) => {
      this.metrics = metrics;
      this.revenue = revenue;
      this.recentFilings = filings.slice(0, 6);
      if (revenue) this.revenueChartData.datasets[0].data = [revenue.totalRevenue, revenue.successfulRevenue, revenue.pendingRevenue];
      this.loading = false;
    });
  }
}
