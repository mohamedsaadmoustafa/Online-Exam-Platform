import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';

import { AuthenticationService } from '../../services/authentication.service';
import { UserModel } from '../../models/user-model';
import {UserRegistrationModel} from "../../models/user-registration-model";
import {AlertService} from "../../services/alert.service";

@Component({
  selector: 'app-exam-start',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent implements OnInit {
  // @ts-ignore
  registerForm: FormGroup;
  loading = false;
  constructor(
    private router: Router,
    private formBuilder: FormBuilder,
    private authenticationService: AuthenticationService,
    private alertService: AlertService
  ) {}

  ngOnInit(): void {
    if (this.authenticationService.isLoggedIn()) {
      this.router.navigate(['/']).then( r =>
        this.alertService.show("Login successful", 'alert-success')
      );
    }

    this.registerForm = this.formBuilder.group({
      firstname: ['', Validators.required],
      lastname: ['', Validators.required],
      username: ['', Validators.required],
      email: ['', Validators.required],
      password: ['', [Validators.required, Validators.minLength(6)]],
      isTeacher: [false],
      role: ['student'],
    });
  }

  getRole(isTeacher: boolean) {
    return isTeacher ? 'teacher' : 'student';
  }

  onSubmit() {
    if (this.registerForm.invalid) {
      return;
    }

    let userRegistration: UserRegistrationModel = {
      firstname: this.registerForm.get('firstname')?.value,
      lastname: this.registerForm.get('lastname')?.value,
      username: this.registerForm.get('username')?.value,
      email: this.registerForm.get('email')?.value,
      password: this.registerForm.get('password')?.value,
      role: this.getRole(this.registerForm.get('role')?.value),
    };
    this.loading = true;

    if (this.registerForm.invalid) {
      return;
    }

    this.authenticationService.register(userRegistration);
    this.loading = false;
  }
}
