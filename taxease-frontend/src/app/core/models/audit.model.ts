export type AuditStatus = 'OPEN' | 'IN_PROGRESS' | 'CLOSED' | 'ESCALATED';

export interface AuditRequest {
  officerId: number;
  taxpayerId?: number;
  scope: string;
}

export interface AuditResponse {
  auditId: number;
  officerId: number;
  taxpayerId: number;
  scope: string;
  findings: string;
  status: AuditStatus;
  createdAt: string;
}

export interface ComplianceRequest {
  entityId: number;
  type: 'FILING' | 'PAYMENT';
  result: string;
  notes?: string;
}

export interface ComplianceResponse {
  complianceId: number;
  entityId: number;
  type: string;
  result: string;
  notes: string;
  recordDate: string;
}

export interface AuditDashboard {
  totalAudits: number;
  openAudits: number;
  closedAudits: number;
  escalatedAudits: number;
  nonComplianceFilings: number;
}
