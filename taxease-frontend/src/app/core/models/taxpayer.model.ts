export interface TaxpayerProfile {
  taxpayerId: number;
  userId: number;
  name: string;
  email: string;
  phone: string;
  taxpayerIdNumber: string;
  panNumber: string;
  type: 'Citizen' | 'Business';
  address: string;
  contactInfo: string;
  createdAt: string;
}

export interface UpdateProfileRequest {
  address?: string;
  contactInfo?: string;
  panNumber?: string;
}

export type DocType = 'IDProof' | 'PAN' | 'BusinessLicense';
export type VerificationStatus = 'Pending' | 'Verified' | 'Rejected';

export interface TaxpayerDocument {
  documentId: number;
  taxpayerId: number;
  docType: DocType;
  fileUri: string;
  verificationStatus: VerificationStatus;
  uploadedDate: string;
  updatedAt: string;
}

export interface DocumentUploadRequest {
  docType: DocType;
  fileUri: string;
}
