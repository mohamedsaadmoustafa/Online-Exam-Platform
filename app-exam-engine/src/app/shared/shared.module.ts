import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HeaderComponent } from './header/header.component';
import {RouterLink} from "@angular/router";



@NgModule({
  declarations: [
    HeaderComponent
  ],
  imports: [
    CommonModule,
    RouterLink,
    RouterLink,
    RouterLink,
    RouterLink,
    RouterLink
  ],
  exports: [HeaderComponent]
})
export class SharedModule { }
