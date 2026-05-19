import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { SharedModule } from '../../shared/shared.module';
import { NotificationListComponent } from './notification-list/notification-list.component';

const routes: Routes = [{ path: '', component: NotificationListComponent }];

@NgModule({
  declarations: [NotificationListComponent],
  imports: [SharedModule, RouterModule.forChild(routes)]
})
export class NotificationsModule {}
