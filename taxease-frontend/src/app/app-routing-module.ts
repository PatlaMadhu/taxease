import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AuthGuard } from './core/guards/auth.guard';
import { RoleGuard } from './core/guards/role.guard';
import { ShellComponent } from './layout/shell.component';

const routes: Routes = [
  {
    path: 'auth',
    loadChildren: () => import('./features/auth/auth.module').then(m => m.AuthModule)
  },
  {
    path: '',
    component: ShellComponent,
    canActivate: [AuthGuard],
    children: [
      {
        path: 'dashboard',
        loadChildren: () => import('./features/dashboard/dashboard.module').then(m => m.DashboardModule)
      },
      {
        path: 'taxpayer',
        loadChildren: () => import('./features/taxpayer/taxpayer.module').then(m => m.TaxpayerModule),
        canActivate: [RoleGuard],
        data: { roles: ['TAXPAYER'] }
      },
      {
        path: 'tax-filing',
        loadChildren: () => import('./features/tax-filing/tax-filing.module').then(m => m.TaxFilingModule),
        canActivate: [RoleGuard],
        data: { roles: ['TAXPAYER', 'OFFICER', 'ADMINISTRATOR', 'MANAGER'] }
      },
      {
        path: 'payment',
        loadChildren: () => import('./features/payment/payment.module').then(m => m.PaymentModule),
        canActivate: [RoleGuard],
        data: { roles: ['TAXPAYER', 'OFFICER', 'ADMINISTRATOR', 'MANAGER'] }
      },
      {
        path: 'audit',
        loadChildren: () => import('./features/audit/audit.module').then(m => m.AuditModule),
        canActivate: [RoleGuard],
        data: { roles: ['OFFICER', 'ADMINISTRATOR', 'COMPLIANCE', 'AUDITOR'] }
      },
      {
        path: 'notifications',
        loadChildren: () => import('./features/notifications/notifications.module').then(m => m.NotificationsModule)
      },
      {
        path: 'reports',
        loadChildren: () => import('./features/reports/reports.module').then(m => m.ReportsModule),
        canActivate: [RoleGuard],
        data: { roles: ['ADMINISTRATOR', 'MANAGER', 'AUDITOR'] }
      },
      {
        path: 'admin',
        loadChildren: () => import('./features/admin/admin.module').then(m => m.AdminModule),
        canActivate: [RoleGuard],
        data: { roles: ['ADMINISTRATOR'] }
      },
      { path: '', redirectTo: 'dashboard', pathMatch: 'full' }
    ]
  },
  { path: 'unauthorized', redirectTo: 'auth/login', pathMatch: 'full' },
  { path: '**', redirectTo: 'auth/login', pathMatch: 'full' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {}
