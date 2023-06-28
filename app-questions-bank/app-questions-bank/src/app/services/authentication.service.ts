import { Injectable } from '@angular/core';
import {Router} from "@angular/router";
import {AuthenticationClient} from "../user/authentication.client";
import {UserLoginModel} from "../models/user-login-model";
import {StorageService} from "./storage.service";
import {catchError, tap} from "rxjs/operators";
import { of } from 'rxjs';
import {AlertService} from "./alert.service";
import {ErrorHandlerService} from "./error-handler.service";


@Injectable({
  providedIn: 'root'
})
export class AuthenticationService {
  //private tokenKey = 'token';

  constructor(
    private storageService: StorageService,
    private authenticationClient: AuthenticationClient,
    private router: Router,
    private alertService: AlertService,
    private errorHandlerService: ErrorHandlerService,

  ) {}

  public login(userLogin: UserLoginModel): void {
    this.authenticationClient.login(userLogin)
      .pipe(
        tap((token) => {
          this.alertService.show('Logged in successfully', 'alert-success');
          this.storageService.saveUser(token);
          this.router.navigate(['/questions']);
        }),
        catchError((error) => {
          this.errorHandlerService.errorHandler(error);
          this.router.navigate(['/login']);
          return of(null);
        }),
      )
      .subscribe();
  }

  public logout() {
    this.alertService.show('Logged out successfully', 'alert-success');
    this.storageService.clean();
    // localStorage.removeItem(this.USER_KEY);
    this.router.navigate(['/login']);
  }

  public isLoggedIn(): boolean {
    //let token = localStorage.getItem(this.tokenKey);
    //return token != null && token.length > 0;
    return this.storageService.isLoggedIn();
  }

  public getToken(): string | null {
    //return this.isLoggedIn() ? localStorage.getItem(this.tokenKey) : null;
    return this.storageService.getUser();
  }
}
