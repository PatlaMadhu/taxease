export type ReportScope = 'FILING' | 'PAYMENT' | 'PROGRAM';

export interface ReportRequest {
  scope: ReportScope;
  title: string;
  metrics: string;
  generatedBy?: number;
}

export interface ReportResponse {
  reportId: number;
  scope: ReportScope;
  title: string;
  metrics: string;
  generatedBy: number;
  generatedDate: string;
}
