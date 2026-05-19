import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { ReactiveFormsModule, FormsModule } from '@angular/forms';
import { MaterialModule } from './material/material.module';
import { SidebarComponent } from './components/sidebar/sidebar.component';
import { TopbarComponent } from './components/topbar/topbar.component';
import { DashboardCardComponent } from './components/dashboard-card/dashboard-card.component';
import { ConfirmDialogComponent } from './components/confirm-dialog/confirm-dialog.component';

@NgModule({
  declarations: [
    SidebarComponent,
    TopbarComponent,
    DashboardCardComponent,
    ConfirmDialogComponent
  ],
  imports: [CommonModule, RouterModule, ReactiveFormsModule, FormsModule, MaterialModule],
  exports: [
    CommonModule, RouterModule, ReactiveFormsModule, FormsModule, MaterialModule,
    SidebarComponent, TopbarComponent, DashboardCardComponent, ConfirmDialogComponent
  ]
})
export class SharedModule {}
