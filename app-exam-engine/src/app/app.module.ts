import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import "@angular/compiler";
import {ExamModule} from "./exam/exam.module";
import { HttpClientModule } from '@angular/common/http';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import {RouterOutlet} from "@angular/router";
import {AuthenticationModule} from "./user/authentication.module";
import { LoggerModule, NgxLoggerLevel } from 'ngx-logger';
import {AngularFireMessagingModule} from "@angular/fire/compat/messaging";
import {AngularFireModule} from "@angular/fire/compat";
import 'firebase/messaging';

const firebaseConfig = {
  apiKey: "AIzaSyCjwGYK55ER3TdgjehITOvkQFykBC_hdqc",
  authDomain: "exam-engine-microservice.firebaseapp.com",
  projectId: "exam-engine-microservice",
  storageBucket: "exam-engine-microservice.appspot.com",
  messagingSenderId: "148184916689",
  appId: "1:148184916689:web:f6ccbc67e2f0e0c6761bf0"
};

@NgModule({
  declarations: [
    AppComponent,
  ],
  imports: [
    AngularFireModule.initializeApp(firebaseConfig),
    AngularFireMessagingModule,
    ExamModule,
    HttpClientModule,
    BrowserModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    RouterOutlet,
    AuthenticationModule,
    LoggerModule.forRoot({
      level: NgxLoggerLevel.DEBUG,
      serverLogLevel: NgxLoggerLevel.ERROR,
      disableConsoleLogging: false,
    }),
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
