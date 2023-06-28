import { Injectable } from '@angular/core';
import { NGXLogger as Logger } from 'ngx-logger';
import jwt_decode from 'jwt-decode';

@Injectable({
  providedIn: 'root'
})
export class StorageService {
  USER_KEY = '';

  constructor(private logger: Logger) {this.logger = logger}

  clean(): void {
    window.sessionStorage.clear();
    // this.logger.info('Session storage cleared');
  }

  public saveUser(user: any): void {
    window.sessionStorage.removeItem(this.USER_KEY);
    window.sessionStorage.setItem(this.USER_KEY, JSON.stringify(user));
    // this.logger.info('User saved to session storage', user);
  }

  public decodeAccessToken(token: string): any {
    let decodedToken = jwt_decode(token);
    return decodedToken;
  }

  public isLoggedIn(): boolean {
    let user = window.sessionStorage.getItem(this.USER_KEY);
    let isLoggedIn = !!user;
    // this.logger.info(`User is ${isLoggedIn ? '' : 'not '}logged in`);
    return isLoggedIn;
  }

  public getUser(): any {
    let user = window.sessionStorage.getItem(this.USER_KEY);
    let result = user ? JSON.parse(user) : {};
    let resultDecoded = this.decodeAccessToken(result)
    // this.logger.info('User retrieved from session storage', resultDecoded);
    return resultDecoded;
  }

  public getUserName(): string {
    return this.getUser().name;
  }

  public getUserEmail(): string {
    return this.getUser().email;
  }
  public getUserRole(): string{
    let roles = this.getUser().realm_access.roles;

    if (roles && roles.includes('student')) {
        return "student";
    }
    else if (roles && roles.includes('teacher')) {
      return "teacher";
    }
    else{
      return "";
    }
  }
}
