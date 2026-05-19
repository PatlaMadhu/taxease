import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { SharedModule } from '../../shared/shared.module';
import { FilingListComponent } from './filing-list/filing-list.component';
import { FilingFormComponent } from './filing-form/filing-form.component';
import { FilingDetailComponent } from './filing-detail/filing-detail.component';

const routes: Routes = [
  { path: '', component: FilingListComponent },
  { path: 'new', component: FilingFormComponent },
  { path: ':id', component: FilingDetailComponent }
];

@NgModule({
  declarations: [FilingListComponent, FilingFormComponent, FilingDetailComponent],
  imports: [SharedModule, RouterModule.forChild(routes)]
})
export class TaxFilingModule {}
