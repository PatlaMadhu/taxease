import { Injectable } from '@angular/core';
import { Observable, of, throwError, shareReplay } from 'rxjs';
import { catchError, tap } from 'rxjs/operators';
import { TaxpayerService } from './taxpayer.service';
import { AuthService } from './auth.service';
import { TaxpayerProfile } from '../models/taxpayer.model';

@Injectable({ providedIn: 'root' })
export class ProfileCacheService {
  private cache$: Observable<TaxpayerProfile> | null = null;

  constructor(private taxpayerService: TaxpayerService, private auth: AuthService) {
    this.auth.registerProfileCache(this);
  }

  getProfile(): Observable<TaxpayerProfile> {
    if (!this.auth.hasRole('TAXPAYER')) return throwError(() => new Error('Not a taxpayer'));
    if (!this.cache$) {
      this.cache$ = this.taxpayerService.getProfile().pipe(
        tap(() => {}),
        catchError(err => {
          this.cache$ = null; // reset so next call retries
          return throwError(() => err);
        }),
        shareReplay(1)
      );
    }
    return this.cache$;
  }

  clear(): void { this.cache$ = null; }
}
