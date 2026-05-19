import { Component, Input, OnInit } from '@angular/core';

@Component({
  standalone: false,
  selector: 'app-dashboard-card',
  template: `
    <div class="stat-card" [ngClass]="'card-' + color">
      <div class="card-inner">
        <div class="card-icon-wrap">
          <mat-icon>{{ icon }}</mat-icon>
        </div>
        <div class="card-body">
          <div class="card-value">{{ value }}</div>
          <div class="card-label">{{ label }}</div>
        </div>
      </div>
      <div class="card-glow"></div>
    </div>
  `,
  styles: [`
    @keyframes valueIn {
      from { opacity:0; transform:translateY(8px); }
      to   { opacity:1; transform:translateY(0); }
    }
    .stat-card {
      background: var(--bg-card);
      border: 1px solid var(--border);
      border-radius: var(--radius-md);
      padding: 22px;
      position: relative;
      overflow: hidden;
      cursor: default;
      transition: transform 0.25s, box-shadow 0.25s, border-color 0.25s;
      animation: fadeInUp 0.5s ease both;

      &:hover {
        transform: translateY(-4px);
        border-color: var(--border-bright);
        box-shadow: var(--shadow-md);
        .card-glow { opacity: 1; }
      }
    }
    .card-inner {
      display: flex; align-items: center; gap: 16px; position: relative; z-index: 1;
    }
    .card-icon-wrap {
      width: 52px; height: 52px; border-radius: 14px;
      display: flex; align-items: center; justify-content: center; flex-shrink: 0;
      mat-icon { font-size: 26px; width: 26px; height: 26px; color: #fff; }
    }
    .card-body {
      min-width: 0;
      .card-value { font-family: 'Syne', sans-serif; font-size: 28px; font-weight: 800; color: var(--text-primary); line-height: 1; animation: valueIn 0.5s ease both; }
      .card-label { font-size: 13px; color: var(--text-muted); margin-top: 5px; font-weight: 500; }
    }
    .card-glow {
      position: absolute; inset: 0; opacity: 0;
      transition: opacity 0.3s;
      pointer-events: none;
    }
    /* Color variants */
    .card-blue .card-icon-wrap { background: linear-gradient(135deg, #3B82F6, #06B6D4); }
    .card-blue .card-glow { background: radial-gradient(circle at 100% 0%, rgba(59,130,246,0.12) 0%, transparent 60%); }
    .card-green .card-icon-wrap { background: linear-gradient(135deg, #059669, #10B981); }
    .card-green .card-glow { background: radial-gradient(circle at 100% 0%, rgba(16,185,129,0.12) 0%, transparent 60%); }
    .card-orange .card-icon-wrap { background: linear-gradient(135deg, #D97706, #F59E0B); }
    .card-orange .card-glow { background: radial-gradient(circle at 100% 0%, rgba(245,158,11,0.12) 0%, transparent 60%); }
    .card-purple .card-icon-wrap { background: linear-gradient(135deg, #7C3AED, #A855F7); }
    .card-purple .card-glow { background: radial-gradient(circle at 100% 0%, rgba(124,58,237,0.12) 0%, transparent 60%); }
    .card-rose .card-icon-wrap { background: linear-gradient(135deg, #DC2626, #F43F5E); }
    .card-rose .card-glow { background: radial-gradient(circle at 100% 0%, rgba(244,63,94,0.12) 0%, transparent 60%); }
    .card-teal .card-icon-wrap { background: linear-gradient(135deg, #0D9488, #06B6D4); }
    .card-teal .card-glow { background: radial-gradient(circle at 100% 0%, rgba(13,148,136,0.12) 0%, transparent 60%); }
    .card-red .card-icon-wrap { background: linear-gradient(135deg, #DC2626, #FF4D4F); }
    .card-red .card-glow { background: radial-gradient(circle at 100% 0%, rgba(220,38,38,0.12) 0%, transparent 60%); }
  `]
})
export class DashboardCardComponent {
  @Input() label = '';
  @Input() value: any = 0;
  @Input() icon = 'star';
  @Input() color: 'blue'|'green'|'orange'|'purple'|'rose'|'teal'|'red' = 'purple';
}
