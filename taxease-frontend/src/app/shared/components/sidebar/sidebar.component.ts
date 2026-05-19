import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../../core/services/auth.service';
import { UserRole } from '../../../core/models/auth.model';

interface NavItem { label: string; icon: string; route: string; roles: UserRole[]; }

@Component({
  standalone: false,
  selector: 'app-sidebar',
  templateUrl: './sidebar.component.html',
  styleUrls: ['./sidebar.component.scss']
})
export class SidebarComponent implements OnInit {
  role: UserRole | null = null;

  navItems: NavItem[] = [
    { label: 'Dashboard',     icon: 'dashboard',            route: '/dashboard',          roles: ['TAXPAYER','OFFICER','ADMINISTRATOR','MANAGER','COMPLIANCE','AUDITOR'] },
    { label: 'My Profile',    icon: 'person',               route: '/taxpayer/profile',   roles: ['TAXPAYER'] },
    { label: 'My Documents',  icon: 'folder',               route: '/taxpayer/documents', roles: ['TAXPAYER'] },
    { label: 'Tax Filings',   icon: 'description',          route: '/tax-filing',         roles: ['TAXPAYER','OFFICER','ADMINISTRATOR','MANAGER'] },
    { label: 'Payments',      icon: 'payment',              route: '/payment',            roles: ['TAXPAYER','OFFICER','ADMINISTRATOR','MANAGER'] },
    { label: 'Audit Cases',   icon: 'gavel',                route: '/audit',              roles: ['OFFICER','ADMINISTRATOR','COMPLIANCE','AUDITOR'] },
    { label: 'Compliance',    icon: 'verified_user',        route: '/audit/compliance',   roles: ['COMPLIANCE','ADMINISTRATOR','AUDITOR'] },
    { label: 'Reports',       icon: 'bar_chart',            route: '/reports',            roles: ['ADMINISTRATOR','MANAGER','AUDITOR'] },
    { label: 'Notifications', icon: 'notifications',        route: '/notifications',      roles: ['TAXPAYER','OFFICER','ADMINISTRATOR','MANAGER','COMPLIANCE','AUDITOR'] },
    { label: 'Audit Logs',    icon: 'history',              route: '/admin/audit-logs',   roles: ['ADMINISTRATOR'] },
    { label: 'Admin Panel',   icon: 'admin_panel_settings', route: '/admin',              roles: ['ADMINISTRATOR'] },
  ];

  constructor(public auth: AuthService, private router: Router) {}

  ngOnInit(): void { this.role = this.auth.getRole(); }

  get visibleItems(): NavItem[] {
    return this.navItems.filter(item => this.role && item.roles.includes(this.role!));
  }

  navigate(route: string): void {
    this.router.navigate([route]);
  }

  isActive(route: string): boolean {
    return this.router.url === route;
  }

  logout(): void { this.auth.logout(); }
}
