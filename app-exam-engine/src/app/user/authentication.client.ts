import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import {Observable} from 'rxjs';
import { environment } from '../../environments/environment';
import {UserLoginModel} from "../models/user-login-model";
import {UserRegistrationModel} from "../models/user-registration-model";


@Injectable({
  providedIn: 'root'
})
export class AuthenticationClient {
  private authenticationApiUrl = environment.authenticationApiUrl;

  constructor(private http: HttpClient) {}

  public login(userLogin: UserLoginModel): Observable<string> {
    let url = `${this.authenticationApiUrl}login`;
    return this.http.post(url , userLogin, { responseType: 'text' });
  }

  public register(userRegestration: UserRegistrationModel): Observable<string> {
    let url = `${this.authenticationApiUrl}register`;
    return this.http.post(url, userRegestration, { responseType: 'text' });
  }
}
