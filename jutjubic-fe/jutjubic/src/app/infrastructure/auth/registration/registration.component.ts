import { Component, OnDestroy, OnInit } from '@angular/core';
import { Validators } from '@angular/forms';
import { Subject } from 'rxjs';
import { AuthService } from '../auth.service';
import { Router, ActivatedRoute } from '@angular/router';
import { FormBuilder, FormControl, FormGroup } from '@angular/forms';
import { takeUntil } from 'rxjs';
import { Registration } from '../model/registration.model';

interface DisplayMessage {
  msgType: 'error' | 'success';
  msgBody: string;
}
@Component({
  selector: 'app-registration',
  templateUrl: './registration.component.html',
  styleUrls: ['./registration.component.css']
})
export class RegistrationComponent {
  registrationForm = new FormGroup({
    firstName: new FormControl('', {
      nonNullable: true,
      validators: [Validators.required]
    }),
    lastName: new FormControl('', {
      nonNullable: true,
      validators: [Validators.required]
    }),
    email: new FormControl('', {
      nonNullable: true,
      validators: [Validators.required, Validators.email]
    }),
    username: new FormControl('', {
      nonNullable: true,
      validators: [Validators.required]
    }),
    address: new FormControl('', {
      nonNullable: true,
      validators: [Validators.required]
    }),
    password1: new FormControl('', {
      nonNullable: true,
      validators: [Validators.required]
    }),
    password2: new FormControl('', {
      nonNullable: true,
      validators: [Validators.required]
    }),
  });

  constructor(
    private authService: AuthService,
    private router: Router
  ) {}

  register(): void {
    const formValue = this.registrationForm.value;

    if (formValue.password1 !== formValue.password2) {
      alert('Passwords must match'); // simple popup
      return;
    }

    if (this.registrationForm.invalid) {
      alert('Please fill all required fields');
      return;
    }

    const registration: Registration = {
      firstName: formValue.firstName!,
      lastName: formValue.lastName!,
      email: formValue.email!,
      username: formValue.username!,
      address: formValue.address!,
      password: formValue.password2!,
    };

    this.authService.register(registration).subscribe({
      next: () => this.router.navigate(['home']),
      error: (err) => {
        console.error(err);
        alert('Registration failed');
      }
    });
  }

  isFormValid(): boolean {
    return !this.registrationForm.invalid && this.registrationForm.value.password1 === this.registrationForm.value.password2;
  }

  get passwordMismatch(): boolean {
    const pw1 = this.registrationForm.get('password1')?.value;
    const pw2 = this.registrationForm.get('password2')?.value;
    return !!pw1 && !!pw2 && pw1 !== pw2;
  }
}