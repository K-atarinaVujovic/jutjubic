import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable, tap } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { TokenStorage } from './jwt/token.service';
import { environment } from 'src/env/environment';
import { JwtHelperService } from '@auth0/angular-jwt';
import { Login } from './model/login.model';
import { AuthenticationResponse } from './model/authentication-response.model';
import { User } from './model/user.model';
import { Registration } from './model/registration.model';
import { User as AccountUser } from 'src/app/feature-modules/user/model/user.model';


@Injectable({ providedIn: 'root' })
export class AuthService {
  user$ = new BehaviorSubject<User>({username: "", id:0});

  constructor(
    private http: HttpClient,
    private tokenStorage: TokenStorage,
    private router: Router,
    private jwtHelper: JwtHelperService
  ) {}

  login(login: Login) {
    return this.http
      .post<AuthenticationResponse>(`${environment.apiHost}/auth/login`, login)
      .pipe(
        tap(res => {
          this.tokenStorage.saveAccessToken(res.accessToken);
          this.setUserFromToken();
          this.router.navigate(['/home']);
        })
      );
  }

  logout() {
    this.tokenStorage.clear();
    this.user$.next({username: "", id: 0});
    this.router.navigate(['/login']);
  }

  register(registration: Registration): Observable<AuthenticationResponse> {
    return this.http
      .post<AuthenticationResponse>(
        `${environment.apiHost}/auth/register`,
        registration
      )
      .pipe(
        tap(res => {
          this.tokenStorage.saveAccessToken(res.accessToken);
          this.setUserFromToken();
          this.router.navigate(['/home']);
        })
      );
  }

  restoreSession() {
    const token = this.tokenStorage.getAccessToken();
    if (!token || this.jwtHelper.isTokenExpired(token)) return;
    this.setUserFromToken();
  }

  private setUserFromToken() {
    const token = this.tokenStorage.getAccessToken()!;
    const decoded = this.jwtHelper.decodeToken(token);

    const user: User = {
      id: decoded.id,
      username: decoded.sub,
    };

    this.user$.next(user);
  }
}
