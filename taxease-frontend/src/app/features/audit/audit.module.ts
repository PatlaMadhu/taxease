import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { SharedModule } from '../../shared/shared.module';
import { RoleGuard } from '../../core/guards/role.guard';
import { AuditListComponent } from './audit-list/audit-list.component';
import { AuditFormComponent } from './audit-form/audit-form.component';
import { ComplianceListComponent } from './compliance-list/compliance-list.component';

const routes: Routes = [
  { path: '', component: AuditListComponent },
  { path: 'new', component: AuditFormComponent, canActivate: [RoleGuard], data: { roles: ['OFFICER', 'ADMINISTRATOR'] } },
  { path: 'compliance', component: ComplianceListComponent, canActivate: [RoleGuard], data: { roles: ['OFFICER', 'ADMINISTRATOR', 'COMPLIANCE', 'AUDITOR'] } }
];

@NgModule({
  declarations: [AuditListComponent, AuditFormComponent, ComplianceListComponent],
  imports: [SharedModule, RouterModule.forChild(routes)]
})
export class AuditModule {}
