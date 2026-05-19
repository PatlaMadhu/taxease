import { Component, OnInit } from '@angular/core';
import { forkJoin, of } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { AuditService } from '../../../core/services/audit.service';
import { AuditResponse, AuditDashboard, ComplianceResponse } from '../../../core/models/audit.model';

@Component({
  standalone: false,
  selector: 'app-compliance-dashboard',
  templateUrl: './compliance-dashboard.component.html',
  styleUrls: ['./compliance-dashboard.component.scss']
})
export class ComplianceDashboardComponent implements OnInit {
  auditDash: AuditDashboard | null = null;
  openAudits: AuditResponse[] = [];
  recentCompliance: ComplianceResponse[] = [];
  loading = true;

  auditChartData: any = {
    labels: ['Open', 'Closed', 'Escalated', 'Non-Compliance'],
    datasets: [{ data: [0, 0, 0, 0], backgroundColor: ['#e65100', '#2e7d32', '#c62828', '#6a1b9a'] }]
  };

  constructor(private auditService: AuditService) {}

  ngOnInit(): void {
    forkJoin({
      audits:     this.auditService.getAllAudits().pipe(catchError(() => of([]))),
      compliance: this.auditService.getAllCompliance().pipe(catchError(() => of([])))
    }).subscribe(({ audits, compliance }) => {
      this.openAudits = audits.filter((a: AuditResponse) => a.status === 'OPEN' || a.status === 'IN_PROGRESS').slice(0, 6);
      this.recentCompliance = compliance.slice(0, 6);
      this.auditDash = {
        totalAudits: audits.length,
        openAudits: audits.filter((a: AuditResponse) => a.status === 'OPEN').length,
        closedAudits: audits.filter((a: AuditResponse) => a.status === 'CLOSED').length,
        escalatedAudits: audits.filter((a: AuditResponse) => a.status === 'ESCALATED').length,
        nonComplianceFilings: compliance.filter((c: ComplianceResponse) => c.result?.toLowerCase().includes('non')).length
      };
      this.auditChartData.datasets[0].data = [
        this.auditDash.openAudits, this.auditDash.closedAudits,
        this.auditDash.escalatedAudits, this.auditDash.nonComplianceFilings
      ];
      this.loading = false;
    });
  }
}
