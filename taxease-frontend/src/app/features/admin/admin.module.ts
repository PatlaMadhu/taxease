import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { SharedModule } from '../../shared/shared.module';
import { UserManagementComponent } from './user-management/user-management.component';
import { AuditLogsComponent } from './audit-logs/audit-logs.component';

const routes: Routes = [
  { path: '', component: UserManagementComponent },
  { path: 'audit-logs', component: AuditLogsComponent }
];

@NgModule({
  declarations: [UserManagementComponent, AuditLogsComponent],
  imports: [SharedModule, RouterModule.forChild(routes)]
})
export class AdminModule {}
