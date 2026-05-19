import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { FilingService } from '../../../core/services/filing.service';
import { ProfileCacheService } from '../../../core/services/profile-cache.service';
import { ToastService } from '../../../core/services/toast.service';

@Component({
  standalone: false,
  selector: 'app-filing-form',
  templateUrl: './filing-form.component.html',
  styleUrls: ['./filing-form.component.scss']
})
export class FilingFormComponent implements OnInit {
  form: FormGroup;
  loading = false;
  taxpayerId!: number;
  taxpayerEmail!: string;
  userId!: number;

  constructor(
    private fb: FormBuilder,
    private filingService: FilingService,
    private profileCache: ProfileCacheService,
    private toast: ToastService,
    private router: Router
  ) {
    this.form = this.fb.group({
      period: ['', Validators.required],
      amountDeclared: ['', [Validators.required, Validators.min(0)]]
    });
  }

  ngOnInit(): void {
    this.profileCache.getProfile().subscribe(p => {
      if (p) {
        this.taxpayerId = p.taxpayerId;
        this.taxpayerEmail = p.email;
        this.userId = p.userId;
      }
    });
  }

  submit(): void {
    if (this.form.invalid) return;
    this.loading = true;
    this.filingService.createFiling({
      taxpayerId: this.taxpayerId,
      userId: this.userId,
      taxpayerEmail: this.taxpayerEmail,
      ...this.form.value
    }).subscribe({
      next: (res) => {
        this.loading = false;
        this.toast.success(`Filing #${res.filingId} submitted successfully!`);
        this.router.navigate(['/tax-filing']);
      },
      error: () => { this.loading = false; }
    });
  }
}
