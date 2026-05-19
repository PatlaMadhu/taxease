import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../../core/services/auth.service';

@Component({
  standalone: false,
  selector: 'app-forgot-password',
  templateUrl: './forgot-password.component.html',
  styleUrls: ['./forgot-password.component.scss']
})
export class ForgotPasswordComponent {
  form: FormGroup;
  loading = false;
  submitted = false;
  resetToken: string | null = null;
  errorMsg: string | null = null;
  hideSecurityAnswer = true;

  constructor(private fb: FormBuilder, private auth: AuthService, private router: Router) {
    this.form = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      securityAnswer: ['', [Validators.required, Validators.minLength(2)]]
    });
  }

  submit(): void {
    if (this.form.invalid) return;
    this.loading = true;
    this.errorMsg = null;

    this.auth.forgotPassword({
      email: this.form.value.email,
      securityAnswer: this.form.value.securityAnswer
    }).subscribe({
      next: (res) => {
        this.loading = false;
        this.submitted = true;
        this.resetToken = res?.resetToken ?? null;
      },
      error: (err) => {
        this.loading = false;
        const code = err?.error?.errorCode;
        if (code === 'WRONG_ANSWER') {
          this.errorMsg = 'Wrong answer. Please try again.';
        } else if (code === 'EMAIL_NOT_FOUND') {
          this.errorMsg = 'No account found with that email address.';
        } else if (code === 'SECURITY_ANSWER_NOT_SET') {
          this.errorMsg = 'This account does not have a security answer set. Please contact support.';
        } else {
          this.errorMsg = err?.error?.message || 'Something went wrong. Please try again.';
        }
      }
    });
  }

  goToReset(): void {
    if (this.resetToken) {
      this.router.navigate(['/auth/reset-password'], { queryParams: { token: this.resetToken } });
    } else {
      this.router.navigate(['/auth/reset-password']);
    }
  }
}
