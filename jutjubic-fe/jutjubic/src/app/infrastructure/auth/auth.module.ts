import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { LoginComponent } from './login/login.component';
import { RegistrationComponent } from './registration/registration.component';
import { MaterialModule } from '../material/material.module';
import { ReactiveFormsModule } from '@angular/forms';
import { UserActivateComponent } from './user-activate/user-activate.component';


@NgModule({
  declarations: [
    LoginComponent,
    RegistrationComponent,
    UserActivateComponent
  ],
  imports: [
    CommonModule,
    MaterialModule,
    ReactiveFormsModule,
  ],
  exports: [
    LoginComponent,
    RegistrationComponent,
    UserActivateComponent,
  ]
})
export class AuthModule { }
