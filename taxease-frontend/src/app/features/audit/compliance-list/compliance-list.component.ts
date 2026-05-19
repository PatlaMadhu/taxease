import { Component, OnInit, ViewChild } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatTableDataSource } from '@angular/material/table';
import { MatPaginator } from '@angular/material/paginator';
import { AuditService } from '../../../core/services/audit.service';
import { ComplianceResponse } from '../../../core/models/audit.model';
import { ToastService } from '../../../core/services/toast.service';

@Component({
  standalone: false,
  selector: 'app-compliance-list',
  templateUrl: './compliance-list.component.html',
  styleUrls: ['./compliance-list.component.scss']
})
export class ComplianceListComponent implements OnInit {
  dataSource = new MatTableDataSource<ComplianceResponse>();
  displayedColumns = ['complianceId', 'entityId', 'type', 'result', 'notes', 'recordDate'];
  loading = true;
  showForm = false;
  form: FormGroup;
  submitting = false;

  @ViewChild(MatPaginator) paginator!: MatPaginator;

  constructor(private auditService: AuditService, private toast: ToastService, private fb: FormBuilder) {
    this.form = this.fb.group({
      entityId: ['', [Validators.required, Validators.min(1)]],
      type: ['', Validators.required],
      result: ['', Validators.required],
      notes: ['']
    });
  }

  ngOnInit(): void {
    this.auditService.getAllCompliance().subscribe({
      next: data => { this.dataSource.data = data; this.dataSource.paginator = this.paginator; this.loading = false; },
      error: () => { this.loading = false; }
    });
  }

  submitCompliance(): void {
    if (this.form.invalid) return;
    this.submitting = true;
    this.auditService.createCompliance(this.form.value).subscribe({
      next: (res) => {
        this.dataSource.data = [res, ...this.dataSource.data];
        this.toast.success('Compliance record created!');
        this.showForm = false;
        this.form.reset();
        this.submitting = false;
      },
      error: () => { this.submitting = false; }
    });
  }
}
