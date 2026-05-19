import { Component, OnInit, ViewChild } from '@angular/core';
import { MatTableDataSource } from '@angular/material/table';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatDialog } from '@angular/material/dialog';
import { AuditService } from '../../../core/services/audit.service';
import { AuditResponse, AuditStatus } from '../../../core/models/audit.model';
import { AuthService } from '../../../core/services/auth.service';
import { ToastService } from '../../../core/services/toast.service';
import { ConfirmDialogComponent } from '../../../shared/components/confirm-dialog/confirm-dialog.component';

@Component({
  standalone: false,
  selector: 'app-audit-list',
  templateUrl: './audit-list.component.html',
  styleUrls: ['./audit-list.component.scss']
})
export class AuditListComponent implements OnInit {
  dataSource = new MatTableDataSource<AuditResponse>();
  displayedColumns = ['auditId', 'officerId', 'taxpayerId', 'scope', 'status', 'createdAt', 'actions'];
  loading = true;
  statuses: AuditStatus[] = ['OPEN', 'IN_PROGRESS', 'CLOSED', 'ESCALATED'];
  canCreate = false;

  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;

  constructor(public auth: AuthService, private auditService: AuditService, private toast: ToastService, private dialog: MatDialog) {}

  ngOnInit(): void {
    this.canCreate = this.auth.hasRole('OFFICER', 'ADMINISTRATOR');
    this.load();
  }

  load(): void {
    this.auditService.getAllAudits().subscribe(data => {
      this.dataSource.data = data;
      this.dataSource.paginator = this.paginator;
      this.dataSource.sort = this.sort;
      this.loading = false;
    });
  }

  applyFilter(event: Event): void {
    this.dataSource.filter = (event.target as HTMLInputElement).value.trim().toLowerCase();
  }

  updateStatus(audit: AuditResponse, status: AuditStatus): void {
    this.auditService.updateAuditStatus(audit.auditId, status).subscribe(() => {
      audit.status = status;
      this.toast.success(`Audit #${audit.auditId} updated to ${status}`);
    });
  }

  closeAudit(audit: AuditResponse): void {
    const ref = this.dialog.open(ConfirmDialogComponent, {
      data: { title: 'Close Audit', message: `Close audit #${audit.auditId}?`, confirmText: 'Close' }
    });
    ref.afterClosed().subscribe(confirmed => {
      if (confirmed) {
        this.auditService.closeAudit(audit.auditId, 'Closed by officer').subscribe(() => {
          audit.status = 'CLOSED';
          this.toast.success(`Audit #${audit.auditId} closed.`);
        });
      }
    });
  }
}
