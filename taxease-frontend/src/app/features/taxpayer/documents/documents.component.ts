import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { TaxpayerService } from '../../../core/services/taxpayer.service';
import { TaxpayerDocument, DocType } from '../../../core/models/taxpayer.model';
import { ToastService } from '../../../core/services/toast.service';
import { ConfirmDialogComponent } from '../../../shared/components/confirm-dialog/confirm-dialog.component';

@Component({
  standalone: false,
  selector: 'app-documents',
  templateUrl: './documents.component.html',
  styleUrls: ['./documents.component.scss']
})
export class DocumentsComponent implements OnInit {
  documents: TaxpayerDocument[] = [];
  loading = true;
  error = false;
  showForm = false;
  form: FormGroup;
  submitting = false;
  docTypes: DocType[] = ['IDProof', 'PAN', 'BusinessLicense'];

  constructor(
    private taxpayerService: TaxpayerService,
    private fb: FormBuilder,
    private toast: ToastService,
    private dialog: MatDialog
  ) {
    this.form = this.fb.group({
      docType: ['', Validators.required],
      fileUri: ['', [Validators.required, Validators.pattern(/^https?:\/\/.+/)]]
    });
  }

  ngOnInit(): void { this.load(); }

  load(): void {
    this.loading = true;
    this.error = false;
    this.taxpayerService.getDocuments().subscribe({
      next: docs => { this.documents = docs; this.loading = false; },
      error: (err) => {
        console.error('Documents load error:', err);
        this.loading = false;
        this.error = true;
      }
    });
  }

  upload(): void {
    if (this.form.invalid) return;
    this.submitting = true;
    this.taxpayerService.uploadDocument(this.form.value).subscribe({
      next: doc => {
        this.documents.push(doc);
        this.toast.success('Document uploaded!');
        this.showForm = false;
        this.form.reset();
        this.submitting = false;
      },
      error: () => { this.submitting = false; }
    });
  }

  delete(doc: TaxpayerDocument): void {
    const ref = this.dialog.open(ConfirmDialogComponent, {
      data: { title: 'Delete Document', message: `Delete ${doc.docType}?`, confirmText: 'Delete' }
    });
    ref.afterClosed().subscribe(confirmed => {
      if (confirmed) {
        this.taxpayerService.deleteDocument(doc.documentId).subscribe(() => {
          this.documents = this.documents.filter(d => d.documentId !== doc.documentId);
          this.toast.success('Document deleted.');
        });
      }
    });
  }
}
