import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { PaymentService } from '../../../core/services/payment.service';
import { ProfileCacheService } from '../../../core/services/profile-cache.service';
import { ToastService } from '../../../core/services/toast.service';
import { PaymentMethod } from '../../../core/models/payment.model';

@Component({
  standalone: false,
  selector: 'app-payment-form',
  templateUrl: './payment-form.component.html',
  styleUrls: ['./payment-form.component.scss']
})
export class PaymentFormComponent implements OnInit {
  form: FormGroup;
  loading = false;
  filingId!: number;
  taxpayerId!: number;
  methods: PaymentMethod[] = ['Bank', 'Wallet', 'UPI', 'UPI_PHONEPE', 'UPI_GPAY', 'UPI_PAYTM', 'NetBanking', 'DebitCard', 'CreditCard'];

  constructor(
    private fb: FormBuilder,
    private paymentService: PaymentService,
    private profileCache: ProfileCacheService,
    private toast: ToastService,
    private router: Router,
    private route: ActivatedRoute
  ) {
    this.form = this.fb.group({
      amount: ['', [Validators.required, Validators.min(0.01)]],
      method: ['', Validators.required]
    });
  }

  ngOnInit(): void {
    this.filingId = Number(this.route.snapshot.paramMap.get('filingId'));
    this.profileCache.getProfile().subscribe(p => { if (p) this.taxpayerId = p.taxpayerId; });
  }

  submit(): void {
    if (this.form.invalid) return;
    this.loading = true;
    this.paymentService.makePayment({ filingId: this.filingId, taxpayerId: this.taxpayerId, ...this.form.value }).subscribe({
      next: (res) => {
        this.loading = false;
        this.toast.success(`Payment #${res.paymentId} initiated successfully!`);
        this.router.navigate(['/payment']);
      },
      error: () => { this.loading = false; }
    });
  }
}
