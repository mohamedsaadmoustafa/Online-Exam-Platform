import { NgModule } from '@angular/core';
import {CommonModule, NgIf} from '@angular/common';
import { QuestionsListComponent } from './questions-list/questions-list.component';
import { QuestionsCountComponent } from './question-count/questions-count.component';
import { SharedModule } from '../shared/shared.module';
import { QuestionDetailsComponent } from './question-details/question-details.component';
import { ReactiveFormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { NgxPaginationModule } from 'ngx-pagination';
import { QuestionEditComponent } from './question-edit/question-edit.component';
import {MaxlengthPipe} from "../maxlength.pipe";

@NgModule({
  declarations: [
    QuestionsListComponent,
    QuestionsCountComponent,
    QuestionDetailsComponent,
    QuestionEditComponent,
    MaxlengthPipe,
  ],
  imports: [
    CommonModule,
    SharedModule,
    RouterModule,
    ReactiveFormsModule,
    NgxPaginationModule,
    NgIf
  ],
  exports: [
    QuestionsListComponent,

  ]
})
export class QuestionsModule { }
