import { Component, EventEmitter, OnInit, Output } from '@angular/core';
import { Router, NavigationEnd } from '@angular/router';
import { filter } from 'rxjs';
import { AuthService } from '../../../core/services/auth.service';
import { ThemeService } from '../../../core/services/theme.service';
import { NotificationService } from '../../../core/services/notification.service';

@Component({
  standalone: false,
  selector: 'app-topbar',
  templateUrl: './topbar.component.html',
  styleUrls: ['./topbar.component.scss']
})
export class TopbarComponent implements OnInit {
  @Output() menuToggle = new EventEmitter<void>();

  notifications: any[] = [];
  unreadCount = 0;
  pageTitle = 'Dashboard';

  private titleMap: Record<string, string> = {
    '/dashboard': 'Dashboard',
    '/taxpayer/profile': 'My Profile',
    '/taxpayer/documents': 'My Documents',
    '/tax-filing': 'Tax Filings',
    '/payment': 'Payments',
    '/audit': 'Audit Cases',
    '/audit/compliance': 'Compliance',
    '/reports': 'Reports & Analytics',
    '/notifications': 'Notifications',
    '/admin': 'Admin Panel',
    '/admin/audit-logs': 'Audit Logs',
  };

  constructor(
    public auth: AuthService,
    public theme: ThemeService,
    private notifService: NotificationService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.router.events.pipe(filter(e => e instanceof NavigationEnd)).subscribe((e: any) => {
      const url = e.urlAfterRedirects?.split('?')[0];
      this.pageTitle = this.titleMap[url] || 'TaxEase';
    });
    this.pageTitle = this.titleMap[this.router.url?.split('?')[0]] || 'TaxEase';

    const userId = this.auth.getUserId();
    if (userId) {
      this.notifService.getUnread(userId).subscribe({
        next: (res: any[]) => {
          this.notifications = (res || []).slice(0, 8);
          this.unreadCount = (res || []).length;
        },
        error: () => {}
      });
    }
  }
}
