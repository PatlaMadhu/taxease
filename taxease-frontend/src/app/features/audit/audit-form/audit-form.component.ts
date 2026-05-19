import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuditService } from '../../../core/services/audit.service';
import { AuthService } from '../../../core/services/auth.service';
import { ToastService } from '../../../core/services/toast.service';

@Component({
  standalone: false,
  selector: 'app-audit-form',
  templateUrl: './audit-form.component.html',
  styleUrls: ['./audit-form.component.scss']
})
export class AuditFormComponent implements OnInit {
  form: FormGroup;
  loading = false;

  constructor(
    private fb: FormBuilder,
    private auditService: AuditService,
    private auth: AuthService,
    private toast: ToastService,
    private router: Router
  ) {
    this.form = this.fb.group({
      officerId: ['', [Validators.required, Validators.min(1)]],
      taxpayerId: [''],
      scope: ['', Validators.required]
    });
  }

  ngOnInit(): void {
    const userId = this.auth.getUserId();
    if (userId) this.form.patchValue({ officerId: userId });
  }

  submit(): void {
    if (this.form.invalid) return;
    this.loading = true;
    this.auditService.createAudit(this.form.value).subscribe({
      next: (res) => {
        this.loading = false;
        this.toast.success(`Audit #${res.auditId} created!`);
        this.router.navigate(['/audit']);
      },
      error: () => { this.loading = false; }
    });
  }
}
