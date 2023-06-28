import { NgModule } from '@angular/core';
import {CommonModule, NgIf} from '@angular/common';
import { HeaderComponent } from './header/header.component';
import {RouterLink} from "@angular/router";



@NgModule({
  declarations: [
    HeaderComponent
  ],
    imports: [
        CommonModule,
        NgIf,
        RouterLink,
        RouterLink
    ],
  exports: [HeaderComponent]
})
export class SharedModule { }
