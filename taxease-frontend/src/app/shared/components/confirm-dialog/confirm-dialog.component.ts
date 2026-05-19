import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';

@Component({
  standalone: false,
  selector: 'app-confirm-dialog',
  template: `
    <div style="padding:28px 32px;min-width:340px">
      <h2 mat-dialog-title style="font-family:'Syne',sans-serif;font-size:20px;font-weight:800;color:var(--text-primary);margin-bottom:12px">
        {{ data.title || 'Confirm Action' }}
      </h2>
      <p style="color:var(--text-muted);font-size:14px;line-height:1.6;margin-bottom:24px">
        {{ data.message || 'Are you sure you want to proceed?' }}
      </p>
      <div style="display:flex;gap:12px;justify-content:flex-end">
        <button mat-stroked-button (click)="ref.close(false)">Cancel</button>
        <button mat-raised-button color="warn" (click)="ref.close(true)"
                style="background:var(--grad-danger)!important;color:#fff!important">
          {{ data.confirm || 'Confirm' }}
        </button>
      </div>
    </div>
  `
})
export class ConfirmDialogComponent {
  constructor(
    public ref: MatDialogRef<ConfirmDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: { title?: string; message?: string; confirm?: string }
  ) {}
}
