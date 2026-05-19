import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AuthService } from '../../../core/services/auth.service';
import { ToastService } from '../../../core/services/toast.service';

@Component({
  standalone: false,
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent {
  form: FormGroup;
  loading = false;
  hidePassword = true;
  errorMsg: string | null = null;

  constructor(private fb: FormBuilder, private auth: AuthService, private toast: ToastService) {
    this.form = this.fb.group({
      email:    ['', [Validators.required, Validators.email]],
      password: ['', Validators.required]
    });
  }

  submit(): void {
    if (this.form.invalid) return;
    this.loading = true;
    this.errorMsg = null;
    this.auth.login(this.form.value).subscribe({
      next: () => { this.loading = false; this.auth.redirectToDashboard(); },
      error: (err) => {
        this.loading = false;
        this.errorMsg = err?.error?.message || 'Invalid email or password. Please try again.';
      }
    });
  }
}
