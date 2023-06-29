import { Component } from '@angular/core';
import { Router } from '@angular/router';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {AuthenticationService} from "../../services/authentication.service";
import {UserModel} from "../../models/user-model";

@Component({
  selector: 'app-exam-start',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  // @ts-ignore
  loginForm: FormGroup;
  loading = false;

  constructor(
    private router: Router,
    private formBuilder: FormBuilder,
    private authenticationService: AuthenticationService
  ) {}

  ngOnInit(): void {
    // redirect to home if already logged in
    if (this.authenticationService.isLoggedIn()) {
      this.router.navigate(['/dashboard']);
    }

    // initialize registerForm object with form controls and validators
    this.loginForm = this.formBuilder.group({
      username: ['', Validators.required],
      password: ['', [Validators.required, Validators.minLength(6)]]
    });
  }

  onSubmit() {
    if (this.loginForm.invalid) {
      return;
    }

    // create user object from form values
    const userLogin= {
      ...this.loginForm.value,
    };

    this.loading = true;
    // stop here if form is invalid
    if (this.loginForm.invalid) {
      return;
    }

    // call the authenticationService to register the user
    this.authenticationService.login(userLogin);
    this.loading = false;
  }
}
