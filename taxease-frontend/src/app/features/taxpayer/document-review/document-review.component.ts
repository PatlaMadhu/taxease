import { Component, OnInit } from '@angular/core';
import { TaxpayerService } from '../../../core/services/taxpayer.service';
import { TaxpayerDocument } from '../../../core/models/taxpayer.model';
import { ToastService } from '../../../core/services/toast.service';

@Component({
  standalone: false,
  selector: 'app-document-review',
  templateUrl: './document-review.component.html',
  styleUrls: ['./document-review.component.scss']
})
export class DocumentReviewComponent implements OnInit {
  documents: TaxpayerDocument[] = [];
  loading = true;
  error = false;

  constructor(private taxpayerService: TaxpayerService, private toast: ToastService) {}

  ngOnInit(): void { this.load(); }

  load(): void {
    this.loading = true;
    this.error = false;
    this.taxpayerService.getAllDocuments().subscribe({
      next: docs => { this.documents = docs; this.loading = false; },
      error: () => { this.error = true; this.loading = false; }
    });
  }

  verify(doc: TaxpayerDocument, status: 'Verified' | 'Rejected'): void {
    this.taxpayerService.verifyDocument(doc.documentId, status).subscribe({
      next: updated => {
        doc.verificationStatus = updated.verificationStatus;
        this.toast.success(`Document #${doc.documentId} marked as ${status}`);
      },
      error: () => {}
    });
  }

  get pendingDocs(): TaxpayerDocument[] {
    return this.documents.filter(d => d.verificationStatus === 'Pending');
  }

  get reviewedDocs(): TaxpayerDocument[] {
    return this.documents.filter(d => d.verificationStatus !== 'Pending');
  }
}
