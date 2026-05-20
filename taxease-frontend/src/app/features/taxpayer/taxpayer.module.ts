import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { SharedModule } from '../../shared/shared.module';
import { ProfileComponent } from './profile/profile.component';
import { DocumentsComponent } from './documents/documents.component';
import { DocumentReviewComponent } from './document-review/document-review.component';

const routes: Routes = [
  { path: 'profile', component: ProfileComponent },
  { path: 'documents', component: DocumentsComponent },
  { path: 'document-review', component: DocumentReviewComponent }
];

@NgModule({
  declarations: [ProfileComponent, DocumentsComponent, DocumentReviewComponent],
  imports: [SharedModule, RouterModule.forChild(routes)]
})
export class TaxpayerModule {}
