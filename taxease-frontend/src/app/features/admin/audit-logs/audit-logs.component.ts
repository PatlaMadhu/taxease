import { Component, OnInit, ViewChild, AfterViewInit } from '@angular/core';
import { MatTableDataSource } from '@angular/material/table';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { AuditLogService } from '../../../core/services/audit-log.service';
import { AuditLogResponse } from '../../../core/models/audit-log.model';

@Component({
  standalone: false,
  selector: 'app-audit-logs',
  templateUrl: './audit-logs.component.html',
  styleUrls: ['./audit-logs.component.scss']
})
export class AuditLogsComponent implements OnInit, AfterViewInit {
  dataSource = new MatTableDataSource<AuditLogResponse>();
  displayedColumns = ['id', 'userId', 'action', 'resource', 'timestamp'];
  loading = true;

  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;

  constructor(private auditLogService: AuditLogService) {}

  ngAfterViewInit(): void {
    this.dataSource.paginator = this.paginator;
    this.dataSource.sort = this.sort;
  }

  ngOnInit(): void {
    this.auditLogService.getAll().subscribe({
      next: data => { this.dataSource.data = data; this.loading = false; },
      error: () => { this.loading = false; }
    });
  }

  applyFilter(event: Event): void {
    this.dataSource.filter = (event.target as HTMLInputElement).value.trim().toLowerCase();
  }
}
