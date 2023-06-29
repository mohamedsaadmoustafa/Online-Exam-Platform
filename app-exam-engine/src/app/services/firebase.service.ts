import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import {Observable, of, throwError} from 'rxjs';
import { catchError, tap } from 'rxjs/operators';
import { AlertService } from './alert.service';
import { Router } from '@angular/router';
import { ErrorHandlerService } from './error-handler.service';
import { environment } from '../../environments/environment';
import { AngularFireMessaging } from '@angular/fire/compat/messaging';

@Injectable({
  providedIn: 'root'
})
export class FirebaseService {
  authenticationApiUrl: string = environment.authenticationApiUrl;

  constructor(
    private http: HttpClient,
    private alertService: AlertService,
    private router: Router,
    private errorHandlerService: ErrorHandlerService,
    private afMessaging: AngularFireMessaging
  ) {}

  public requestPermission(username: string): void {
    this.afMessaging.requestToken.subscribe(
      (token) => {
        console.log('Permission granted! Save to server!', token);
        this.sendTokenToBackend(username, token)
      },
      (error) => console.error(error)
    );
  }

  public listenForMessages(callback: (payload: any) => void): void {
    this.afMessaging.onMessage((payload) => {
      console.log('Message received. ', payload);
      this.alertService.show(payload.data.message, 'alert-success');
      callback(payload); // Call the callback function with the payload
    });
  }

  public async sendTokenToBackend(username: string, token: any) {
    try {
      const url = `${this.authenticationApiUrl}${username}/token`;
      const tokenString = token; // Convert token to a string

      const response = await fetch(url, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/string',
        },
        body: token,
      });

      if (!response.ok) {
        throw new Error('Failed to send token to backend.');
      }
    } catch (error) {
      console.error('Error while sending token to backend:', error);
    }
  }
}
