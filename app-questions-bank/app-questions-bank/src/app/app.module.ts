import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { QuestionsService } from './services/questions.service';
import { QuestionsModule } from './questions/questions.module';
import { HttpClientModule } from '@angular/common/http';
import { BrowserAnimationsModule}  from '@angular/platform-browser/animations'
import {FormsModule} from "@angular/forms";
import {HeaderComponent} from "./shared/header/header.component";
import {AuthenticationModule} from "./user/authentication.module";
import {LoggerModule, NgxLoggerLevel} from "ngx-logger";

@NgModule({
  declarations: [
    AppComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    QuestionsModule,
    HttpClientModule,
    AuthenticationModule,
    LoggerModule.forRoot({
      level: NgxLoggerLevel.DEBUG,
      serverLogLevel: NgxLoggerLevel.ERROR,
      disableConsoleLogging: false,
    }),
    BrowserAnimationsModule,
    FormsModule
  ],
  providers: [QuestionsService],
  exports: [

  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
