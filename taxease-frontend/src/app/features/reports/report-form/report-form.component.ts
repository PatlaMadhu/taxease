import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { ReportService } from '../../../core/services/report.service';
import { ToastService } from '../../../core/services/toast.service';
import { ReportScope } from '../../../core/models/report.model';

@Component({
  standalone: false,
  selector: 'app-report-form',
  templateUrl: './report-form.component.html',
  styleUrls: ['./report-form.component.scss']
})
export class ReportFormComponent {
  form: FormGroup;
  loading = false;
  scopes: ReportScope[] = ['FILING', 'PAYMENT', 'PROGRAM'];

  constructor(private fb: FormBuilder, private reportService: ReportService, private toast: ToastService, private router: Router) {
    this.form = this.fb.group({
      title: ['', Validators.required],
      scope: ['', Validators.required],
      metrics: ['', Validators.required]
    });
  }

  submit(): void {
    if (this.form.invalid) return;
    this.loading = true;
    this.reportService.generateReport(this.form.value).subscribe({
      next: (res) => {
        this.loading = false;
        this.toast.success(`Report #${res.reportId} generated!`);
        this.router.navigate(['/reports']);
      },
      error: () => { this.loading = false; }
    });
  }
}
