import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from './auth.service';
import { User } from './model/user.model';

export const authGuard: CanActivateFn = (route, state) => {
  const router = inject(Router);
  const authService = inject(AuthService);

  const user: User = authService.user$.getValue();

  // Not logged in
  if (!user || user.username === '') {
    router.navigate(['login']);
    return false;
  }

  // // Role-based check
  // const allowedRoles = route.data?.['roles'] as string[] | undefined;
  // if (allowedRoles && !allowedRoles.includes(user.role)) {
  //   router.navigate(['home']);
  //   return false;
  // }

  return true;
};
