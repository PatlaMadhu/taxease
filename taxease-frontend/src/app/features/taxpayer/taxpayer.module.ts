import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { SharedModule } from '../../shared/shared.module';
import { ProfileComponent } from './profile/profile.component';
import { DocumentsComponent } from './documents/documents.component';

const routes: Routes = [
  { path: 'profile', component: ProfileComponent },
  { path: 'documents', component: DocumentsComponent }
];

@NgModule({
  declarations: [ProfileComponent, DocumentsComponent],
  imports: [SharedModule, RouterModule.forChild(routes)]
})
export class TaxpayerModule {}
