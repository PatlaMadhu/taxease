import { Component, OnInit, ViewChild, AfterViewInit } from '@angular/core';
import { MatTableDataSource } from '@angular/material/table';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { FilingService } from '../../../core/services/filing.service';
import { ProfileCacheService } from '../../../core/services/profile-cache.service';
import { AuthService } from '../../../core/services/auth.service';
import { FilingResponse, FilingStatus } from '../../../core/models/filing.model';
import { ToastService } from '../../../core/services/toast.service';

@Component({
  standalone: false,
  selector: 'app-filing-list',
  templateUrl: './filing-list.component.html',
  styleUrls: ['./filing-list.component.scss']
})
export class FilingListComponent implements OnInit, AfterViewInit {
  dataSource = new MatTableDataSource<FilingResponse>();
  displayedColumns = ['filingId', 'period', 'amountDeclared', 'status', 'submittedDate', 'actions'];
  loading = true;
  isOfficer = false;
  statuses: FilingStatus[] = ['DRAFT','PENDING','SUBMITTED','APPROVED','REJECTED','ACTIVE','INACTIVE'];
  officerStatuses: FilingStatus[] = ['APPROVED', 'REJECTED'];

  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;

  constructor(
    private filingService: FilingService,
    private profileCache: ProfileCacheService,
    public auth: AuthService,
    private toast: ToastService
  ) {}

  ngAfterViewInit(): void {
    this.dataSource.paginator = this.paginator;
    this.dataSource.sort = this.sort;
  }

  ngOnInit(): void {
    this.isOfficer = this.auth.hasRole('OFFICER', 'ADMINISTRATOR', 'MANAGER', 'COMPLIANCE', 'AUDITOR');
    this.filingService.invalidate();
    if (this.isOfficer) {
      this.filingService.getAllFilings().subscribe(data => {
        this.dataSource.data = data;
        this.loading = false;
      });
    } else {
      this.profileCache.getProfile().subscribe({
        next: profile => {
          if (!profile) { this.loading = false; return; }
          this.filingService.getFilingsByTaxpayer(profile.taxpayerId).subscribe(data => {
            this.dataSource.data = data;
            this.loading = false;
          });
        },
        error: () => { this.loading = false; }
      });
    }
  }

  applyFilter(event: Event): void {
    this.dataSource.filter = (event.target as HTMLInputElement).value.trim().toLowerCase();
  }

  filterByStatus(status: FilingStatus | ''): void {
    this.dataSource.filter = status.toLowerCase();
  }

  updateStatus(filing: FilingResponse, status: FilingStatus): void {
    this.filingService.updateFilingStatus(filing.filingId, status).subscribe(() => {
      filing.status = status;
      this.toast.success(`Filing #${filing.filingId} updated to ${status}`);
    });
  }

  submitFiling(filing: FilingResponse): void {
    this.filingService.submitFiling(filing.filingId).subscribe(updated => {
      filing.status = updated.status;
      this.toast.success('Filing submitted successfully!');
    });
  }
}
