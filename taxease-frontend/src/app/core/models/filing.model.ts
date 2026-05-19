export type FilingStatus = 'DRAFT' | 'PENDING' | 'SUBMITTED' | 'APPROVED' | 'REJECTED' | 'ACTIVE' | 'INACTIVE';

export interface FilingRequest {
  taxpayerId: number;
  userId?: number;
  taxpayerEmail: string;
  period: string;
  amountDeclared: number;
}

export interface FilingResponse {
  filingId: number;
  taxpayerId: number;
  taxpayerEmail: string;
  period: string;
  amountDeclared: number;
  status: FilingStatus;
  submittedDate: string;
}

export interface FilingDocument {
  documentId: number;
  filingId: number;
  docType: string;
  fileUri: string;
  verificationStatus: string;
  uploadedDate: string;
}

export interface DocumentUploadRequest {
  docType: string;
  fileUri: string;
}
