import { Injectable } from '@angular/core';
import { Observable, of, shareReplay } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { TaxpayerService } from './taxpayer.service';
import { AuthService } from './auth.service';
import { TaxpayerProfile } from '../models/taxpayer.model';

@Injectable({ providedIn: 'root' })
export class ProfileCacheService {
  private cache$: Observable<TaxpayerProfile | null> | null = null;

  constructor(private taxpayerService: TaxpayerService, private auth: AuthService) {
    this.auth.registerProfileCache(this);
  }

  getProfile(): Observable<TaxpayerProfile | null> {
    if (!this.auth.hasRole('TAXPAYER')) return of(null);
    if (!this.cache$) {
      this.cache$ = this.taxpayerService.getProfile().pipe(
        catchError(() => of(null)),
        shareReplay(1)
      );
    }
    return this.cache$;
  }

  clear(): void { this.cache$ = null; }
}
