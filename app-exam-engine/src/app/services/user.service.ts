import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import {Observable, of} from 'rxjs';
import {environment} from "../../environments/environment";
import {UserModel} from "../models/user-model";


@Injectable({
  providedIn: 'root',
})
export class UserService {
  private authenticationApiUrl = environment.authenticationApiUrl;

  constructor(private http: HttpClient) {}

  public getAllStudents() {
    const url = `${this.authenticationApiUrl}student`;
    return this.http.get<UserModel[]>(url);
  }
}
