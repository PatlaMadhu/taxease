import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { BehaviorSubject, Observable, tap } from 'rxjs';
import { environment } from '../../../environments/environment';
import {
  LoginRequest, LoginResponse,
  RegisterRequest, RegisterResponse,
  ForgotPasswordRequest, ForgotPasswordResponse,
  JwtPayload, UserRole
} from '../models/auth.model';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private readonly TOKEN_KEY = 'taxease_token';
  private roleSubject = new BehaviorSubject<UserRole | null>(this.getRole());
  role$ = this.roleSubject.asObservable();

  private profileCache?: { clear(): void };

  registerProfileCache(cache: { clear(): void }): void {
    this.profileCache = cache;
  }

  constructor(private http: HttpClient, private router: Router) {}

  login(payload: LoginRequest): Observable<LoginResponse> {
    return this.http.post<LoginResponse>(`${environment.apiUrl}/auth/login`, payload).pipe(
      tap(res => {
        localStorage.setItem(this.TOKEN_KEY, res.token);
        this.roleSubject.next(this.getRole());
      })
    );
  }

  register(payload: RegisterRequest): Observable<RegisterResponse> {
    return this.http.post<RegisterResponse>(`${environment.apiUrl}/auth/register`, payload);
  }

  /**
   * Forgot-password: sends email + security answer to backend.
   * Backend verifies answer; returns resetToken on success.
   * Throws HTTP 400 with errorCode='WRONG_ANSWER' if answer is incorrect.
   */
  forgotPassword(payload: ForgotPasswordRequest): Observable<ForgotPasswordResponse> {
    return this.http.post<ForgotPasswordResponse>(`${environment.apiUrl}/auth/forgot-password`, payload);
  }

  resetPassword(token: string, newPassword: string): Observable<{ message: string }> {
    return this.http.post<{ message: string }>(`${environment.apiUrl}/auth/reset-password`, { token, newPassword });
  }

  logout(): void {
    localStorage.removeItem(this.TOKEN_KEY);
    this.roleSubject.next(null);
    this.profileCache?.clear();
    this.router.navigate(['/auth/login']);
  }

  getToken(): string | null {
    return localStorage.getItem(this.TOKEN_KEY);
  }

  isLoggedIn(): boolean {
    const token = this.getToken();
    if (!token) return false;
    const payload = this.decodeToken(token);
    return payload ? payload.exp * 1000 > Date.now() : false;
  }

  getRole(): UserRole | null {
    const token = this.getToken();
    if (!token) return null;
    return this.decodeToken(token)?.role as UserRole ?? null;
  }

  getEmail(): string | null {
    const token = this.getToken();
    if (!token) return null;
    return this.decodeToken(token)?.sub ?? null;
  }

  getUserId(): number | null {
    const token = this.getToken();
    if (!token) return null;
    return this.decodeToken(token)?.userId ?? null;
  }

  hasRole(...roles: UserRole[]): boolean {
    const role = this.getRole();
    return role ? roles.includes(role) : false;
  }

  redirectToDashboard(): void {
    const role = this.getRole();
    const routes: Record<string, string> = {
      TAXPAYER:      '/dashboard/taxpayer',
      OFFICER:       '/dashboard/officer',
      ADMINISTRATOR: '/dashboard/admin',
      MANAGER:       '/dashboard/manager',
      COMPLIANCE:    '/dashboard/compliance',
      AUDITOR:       '/dashboard/auditor',
    };
    const target = role && routes[role] ? routes[role] : '/auth/login';
    this.router.navigate([target]);
  }

  private decodeToken(token: string): JwtPayload | null {
    try {
      const payload = token.split('.')[1];
      return JSON.parse(atob(payload));
    } catch {
      return null;
    }
  }
}
