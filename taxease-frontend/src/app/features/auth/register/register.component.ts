import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../../core/services/auth.service';
import { ToastService } from '../../../core/services/toast.service';

@Component({
  standalone: false,
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss']
})
export class RegisterComponent {
  form: FormGroup;
  loading = false;
  hidePassword = true;
  hideSecurityAnswer = true;
  taxpayerTypes = ['Citizen', 'Business'];

  constructor(private fb: FormBuilder, private auth: AuthService, private toast: ToastService, private router: Router) {
    this.form = this.fb.group({
      name: ['', [Validators.required, Validators.minLength(2), Validators.maxLength(150)]],
      email: ['', [Validators.required, Validators.email]],
      phone: ['', [Validators.required, Validators.pattern(/^\+?[0-9]{10,15}$/)]],
      password: ['', [Validators.required, Validators.minLength(8)]],
      taxpayerType: ['', Validators.required],
      address: ['', [Validators.required, Validators.maxLength(500)]],
      securityAnswer: ['', [Validators.required, Validators.minLength(2), Validators.maxLength(200)]]
    });
  }

  submit(): void {
    if (this.form.invalid) return;
    this.loading = true;
    const payload = { ...this.form.value, contactInfo: this.form.value.phone };
    this.auth.register(payload).subscribe({
      next: (res) => {
        this.loading = false;
        this.toast.success(`Registration successful! Your Tax ID: ${res.taxpayerIdNumber}`);
        this.router.navigate(['/auth/login']);
      },
      error: () => { this.loading = false; }
    });
  }
}
