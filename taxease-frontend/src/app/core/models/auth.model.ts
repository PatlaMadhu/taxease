export interface LoginRequest {
  email: string;
  password: string;
}

export interface RegisterRequest {
  name: string;
  email: string;
  phone: string;
  password: string;
  taxpayerType: 'Citizen' | 'Business';
  address: string;
  contactInfo: string;
  /** Answer to "What is your favorite place?" */
  securityAnswer: string;
}

export interface LoginResponse {
  token: string;
}

export interface RegisterResponse {
  taxpayerIdNumber: number;
  name: string;
  email: string;
}

export interface ForgotPasswordRequest {
  email: string;
  securityAnswer: string;
}

export interface ForgotPasswordResponse {
  message: string;
  resetToken?: string;
  errorCode?: string;
}

export interface JwtPayload {
  sub: string;
  role: string;
  userId: number;
  exp: number;
}

export type UserRole = 'TAXPAYER' | 'OFFICER' | 'ADMINISTRATOR' | 'MANAGER' | 'COMPLIANCE' | 'AUDITOR';
