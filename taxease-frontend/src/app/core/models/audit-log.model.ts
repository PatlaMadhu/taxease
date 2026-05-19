export interface AuditLogResponse {
  id: number;
  userId: number;
  action: string;
  resource: string;
  timestamp: string;
}
