import { Component, OnInit, ChangeDetectionStrategy, ChangeDetectorRef } from '@angular/core';
import { forkJoin, of } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { FilingService } from '../../../core/services/filing.service';
import { AuditService } from '../../../core/services/audit.service';
import { PaymentService } from '../../../core/services/payment.service';
import { FilingResponse } from '../../../core/models/filing.model';
import { AuditResponse } from '../../../core/models/audit.model';
import { PaymentMetrics } from '../../../core/models/payment.model';

@Component({
  standalone: false,
  selector: 'app-officer-dashboard',
  templateUrl: './officer-dashboard.component.html',
  styleUrls: ['./officer-dashboard.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class OfficerDashboardComponent implements OnInit {
  pendingFilings: FilingResponse[] = [];
  openAudits: AuditResponse[] = [];
  metrics: PaymentMetrics | null = null;
  loading = true;

  constructor(
    private filingService: FilingService,
    private auditService: AuditService,
    private paymentService: PaymentService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    forkJoin({
      pending: this.filingService.getFilingsByStatus('SUBMITTED').pipe(catchError(() => of([]))),
      audits:  this.auditService.getAllAudits().pipe(catchError(() => of([]))),
      metrics: this.paymentService.getPaymentMetrics().pipe(catchError(() => of(null)))
    }).subscribe(({ pending, audits, metrics }) => {
      this.pendingFilings = pending;
      this.openAudits = audits.filter((a: AuditResponse) => a.status === 'OPEN' || a.status === 'IN_PROGRESS');
      this.metrics = metrics;
      this.loading = false;
      this.cdr.markForCheck();
    });
  }
}
