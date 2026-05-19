import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { SharedModule } from '../../shared/shared.module';
import { BaseChartDirective } from 'ng2-charts';
import { ReportListComponent } from './report-list/report-list.component';
import { ReportFormComponent } from './report-form/report-form.component';

const routes: Routes = [
  { path: '', component: ReportListComponent },
  { path: 'new', component: ReportFormComponent }
];

@NgModule({
  declarations: [ReportListComponent, ReportFormComponent],
  imports: [SharedModule, BaseChartDirective, RouterModule.forChild(routes)]
})
export class ReportsModule {}
