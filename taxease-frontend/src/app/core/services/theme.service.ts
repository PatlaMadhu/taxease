import { Injectable } from '@angular/core';

@Injectable({ providedIn: 'root' })
export class ThemeService {
  private readonly KEY = 'taxease-theme';
  isDark = true;

  constructor() {
    const saved = localStorage.getItem(this.KEY);
    this.isDark = saved ? saved === 'dark' : true;
    this.apply();
  }

  toggle(): void {
    this.isDark = !this.isDark;
    localStorage.setItem(this.KEY, this.isDark ? 'dark' : 'light');
    this.apply();
  }

  private apply(): void {
    document.body.classList.toggle('light-mode', !this.isDark);
  }
}
