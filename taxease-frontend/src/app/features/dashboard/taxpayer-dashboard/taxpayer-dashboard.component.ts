import { Component, OnInit, ChangeDetectionStrategy, ChangeDetectorRef } from '@angular/core';
import { forkJoin, of } from 'rxjs';
import { catchError, switchMap } from 'rxjs/operators';
import { ProfileCacheService } from '../../../core/services/profile-cache.service';
import { FilingService } from '../../../core/services/filing.service';
import { PaymentService } from '../../../core/services/payment.service';
import { TaxpayerProfile } from '../../../core/models/taxpayer.model';
import { FilingResponse } from '../../../core/models/filing.model';
import { PaymentResponse } from '../../../core/models/payment.model';

@Component({
  standalone: false,
  selector: 'app-taxpayer-dashboard',
  templateUrl: './taxpayer-dashboard.component.html',
  styleUrls: ['./taxpayer-dashboard.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class TaxpayerDashboardComponent implements OnInit {
  profile: TaxpayerProfile | null = null;
  filings: FilingResponse[] = [];
  payments: PaymentResponse[] = [];
  loading = true;
  error = false;

  get totalFiled(): number { return this.filings.length; }
  get pendingFilings(): number { return this.filings.filter(f => f.status === 'PENDING' || f.status === 'DRAFT').length; }
  get approvedFilings(): number { return this.filings.filter(f => f.status === 'APPROVED').length; }
  get totalPaid(): number { return this.payments.filter(p => p.status === 'Completed').reduce((s, p) => s + p.amount, 0); }

  constructor(
    private profileCache: ProfileCacheService,
    private filingService: FilingService,
    private paymentService: PaymentService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void { this.load(); }

  load(): void {
    this.loading = true;
    this.error = false;
    this.profileCache.getProfile().pipe(
      switchMap(profile => {
        this.profile = profile;
        return forkJoin({
          filings: this.filingService.getFilingsByTaxpayer(profile.taxpayerId).pipe(catchError(() => of([]))),
          payments: this.paymentService.getPaymentHistory(profile.taxpayerId).pipe(catchError(() => of([])))
        });
      }),
      catchError(() => {
        this.error = true;
        this.loading = false;
        this.cdr.markForCheck();
        return of({ filings: [], payments: [] });
      })
    ).subscribe(({ filings, payments }) => {
      this.filings = filings as FilingResponse[];
      this.payments = payments as PaymentResponse[];
      this.loading = false;
      this.cdr.markForCheck();
    });
  }
}
