import { Injectable } from '@angular/core';
import { HttpInterceptor, HttpRequest, HttpHandler, HttpEvent, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { AuthService } from '../services/auth.service';
import { ToastService } from '../services/toast.service';

@Injectable()
export class ErrorInterceptor implements HttpInterceptor {
  constructor(private auth: AuthService, private toast: ToastService) {}

  /** URLs where the component handles errors itself — no global toast */
  private readonly silentUrls = [
    '/auth/forgot-password',
    '/auth/reset-password',
    '/auth/login',
    '/auth/register',
  ];

  intercept(req: HttpRequest<unknown>, next: HttpHandler): Observable<HttpEvent<unknown>> {
    const isSilent = this.silentUrls.some(u => req.url.includes(u));

    return next.handle(req).pipe(
      catchError((err: HttpErrorResponse) => {
        if (err.status === 401) {
          const isPublicRoute = this.silentUrls.some(u => req.url.includes(u));
          if (!isPublicRoute) this.auth.logout();
        } else if (!isSilent) {
          if (err.status === 400) {
            const message = err.error?.message || err.error?.error || 'Invalid request.';
            this.toast.error(typeof message === 'string' ? message : 'Invalid request.');
          } else if (err.status === 403) {
            this.toast.error('Access denied. Insufficient permissions.');
          } else if (err.status === 404) {
            const message = err.error?.message || 'Resource not found.';
            this.toast.error(typeof message === 'string' ? message : 'Resource not found.');
          } else if (err.status === 0) {
            this.toast.error('Unable to connect to server. Please try again.');
          } else if (err.status >= 500) {
            const message = err.error?.error || err.error?.message || 'Server error. Please try again.';
            this.toast.error(message);
          }
        }
        return throwError(() => err);
      })
    );
  }
}
