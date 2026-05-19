import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { TaxpayerService } from '../../../core/services/taxpayer.service';
import { TaxpayerProfile } from '../../../core/models/taxpayer.model';
import { ToastService } from '../../../core/services/toast.service';

@Component({
  standalone: false,
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.scss']
})
export class ProfileComponent implements OnInit {
  profile: TaxpayerProfile | null = null;
  form: FormGroup;
  loading = true;
  saving = false;
  editMode = false;
  error = false;

  constructor(private taxpayerService: TaxpayerService, private fb: FormBuilder, private toast: ToastService) {
    this.form = this.fb.group({
      address:     ['', Validators.maxLength(500)],
      contactInfo: ['', Validators.maxLength(200)],
      panNumber:   ['', Validators.maxLength(20)]
    });
  }

  ngOnInit(): void {
    this.loadProfile();
  }

  loadProfile(): void {
    this.loading = true;
    this.error = false;
    this.taxpayerService.getProfile().subscribe({
      next: p => {
        this.profile = p;
        this.form.patchValue({ address: p.address, contactInfo: p.contactInfo, panNumber: p.panNumber });
        this.loading = false;
      },
      error: () => { this.loading = false; this.error = true; }
    });
  }

  reload(): void { this.loadProfile(); }

  save(): void {
    if (this.form.invalid) return;
    this.saving = true;
    this.taxpayerService.updateProfile(this.form.value).subscribe({
      next: p => { this.profile = p; this.editMode = false; this.saving = false; this.toast.success('Profile updated!'); },
      error: () => { this.saving = false; }
    });
  }
}
