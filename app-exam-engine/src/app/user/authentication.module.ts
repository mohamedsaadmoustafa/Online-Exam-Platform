import { NgModule } from '@angular/core';
import {CommonModule, NgForOf} from '@angular/common';
import { SharedModule } from '../shared/shared.module';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import { RouterModule } from '@angular/router';
import {LoginComponent} from "./user-login/login.component";
import {RegisterComponent} from "./user-register/register.component";

@NgModule({
  declarations: [
    LoginComponent,
    RegisterComponent
  ],
  imports: [
    CommonModule,
    SharedModule,
    RouterModule,
    ReactiveFormsModule,
    SharedModule,
    NgForOf,
    ReactiveFormsModule,
    FormsModule,
  ],
  exports: [
    LoginComponent,
    RegisterComponent
  ]
})
export class AuthenticationModule { }
