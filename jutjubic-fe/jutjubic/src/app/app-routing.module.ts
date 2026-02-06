import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { LoginComponent } from './infrastructure/auth/login/login.component';
import { RegistrationComponent } from './infrastructure/auth/registration/registration.component';
import { HomeComponent } from './feature-modules/layout/home/home.component';
import { UserActivateComponent } from './infrastructure/auth/user-activate/user-activate.component';
import { VideoViewComponent } from './feature-modules/video/video-view/video-view.component';

const routes: Routes = [
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegistrationComponent },
  { path: 'home', component: HomeComponent },
  { path: 'activate', component: UserActivateComponent },
  { path: 'video/:id', component: VideoViewComponent },
  { path: '**', redirectTo: 'login' },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
