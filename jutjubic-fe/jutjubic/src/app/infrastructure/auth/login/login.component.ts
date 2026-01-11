import { Component, OnInit } from '@angular/core';
import { FormGroup, FormBuilder, Validators, FormControl } from '@angular/forms';
import { AuthService } from '../auth.service';
import { ActivatedRoute, Router } from '@angular/router';
import { Login } from '../model/login.model';

interface DisplayMessage {
  msgType: 'error' | 'success';
  msgBody: string;
}

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  loginForm = new FormGroup({
    email: new FormControl('', [Validators.required]),
    password: new FormControl('', [Validators.required]),
  });

  returnUrl = '/';
  notification?: DisplayMessage;
  submitted = false;

  constructor(
    private authService: AuthService,
    private router: Router,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      if (params['msg']) {
        this.notification = {
          msgType: 'error',
          msgBody: params['msg']
        };
      }
    });

    // this.returnUrl =
    //   this.route.snapshot.queryParams['returnUrl'] || '/';

    // // Optional: redirect if already logged in
    // if (this.authService.currentUser) {
    //   this.router.navigate([this.returnUrl]);
    // }
  }

  login(): void {
    if (!this.loginForm.valid) return;

    this.submitted = true;
    this.notification = undefined;

    const login: Login = this.loginForm.value as Login;

    this.authService.login(login).subscribe({
      next: () => {
        this.router.navigate(["/home"]);
      },
      error: () => {
        this.submitted = false;
        this.notification = {
          msgType: 'error',
          msgBody: 'Incorrect email or password.'
        };
      }
    });
  }
}