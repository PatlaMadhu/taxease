import { NgModule, Injectable } from '@angular/core';
import { RouterModule, Routes, CanActivate } from '@angular/router';
import { SharedModule } from '../../shared/shared.module';
import { BaseChartDirective } from 'ng2-charts';
import { RoleGuard } from '../../core/guards/role.guard';
import { TaxpayerDashboardComponent } from './taxpayer-dashboard/taxpayer-dashboard.component';
import { OfficerDashboardComponent } from './officer-dashboard/officer-dashboard.component';
import { AdminDashboardComponent } from './admin-dashboard/admin-dashboard.component';
import { ManagerDashboardComponent } from './manager-dashboard/manager-dashboard.component';
import { ComplianceDashboardComponent } from './compliance-dashboard/compliance-dashboard.component';
import { AuthService } from '../../core/services/auth.service';
import { Router } from '@angular/router';

@Injectable({ providedIn: 'root' })
export class DashboardRedirectGuard implements CanActivate {
  constructor(private auth: AuthService, private router: Router) {}
  canActivate(): boolean {
    this.auth.redirectToDashboard();
    return false;
  }
}

const routes: Routes = [
  { path: 'taxpayer',   component: TaxpayerDashboardComponent,   canActivate: [RoleGuard], data: { roles: ['TAXPAYER'] } },
  { path: 'officer',    component: OfficerDashboardComponent,    canActivate: [RoleGuard], data: { roles: ['OFFICER'] } },
  { path: 'admin',      component: AdminDashboardComponent,      canActivate: [RoleGuard], data: { roles: ['ADMINISTRATOR'] } },
  { path: 'manager',    component: ManagerDashboardComponent,    canActivate: [RoleGuard], data: { roles: ['MANAGER'] } },
  { path: 'compliance', component: ComplianceDashboardComponent, canActivate: [RoleGuard], data: { roles: ['COMPLIANCE'] } },
  { path: 'auditor',    component: ComplianceDashboardComponent, canActivate: [RoleGuard], data: { roles: ['AUDITOR'] } },
  { path: '', canActivate: [DashboardRedirectGuard], children: [] }
];

@NgModule({
  declarations: [
    TaxpayerDashboardComponent,
    OfficerDashboardComponent,
    AdminDashboardComponent,
    ManagerDashboardComponent,
    ComplianceDashboardComponent
  ],
  imports: [SharedModule, BaseChartDirective, RouterModule.forChild(routes)]
})
export class DashboardModule {}
