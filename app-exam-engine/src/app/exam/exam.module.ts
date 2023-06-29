import { NgModule } from '@angular/core';
import {CommonModule, NgForOf, NgIf} from '@angular/common';
import { SharedModule } from '../shared/shared.module';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import { RouterModule } from '@angular/router';
import {ExamTeacherComponent} from "./exam-teacher/exam-teacher.component";
import {ExamStudentComponent} from "./exam-student/exam-student.component";
import {ExamInstanceComponent} from "./exam-instance/exam-instance.component";
import {ExamDefinitionComponent} from "./exam-definition/exam-definition.component";
import {ExamStartExamComponent} from "./exam-start-exam/exam-start-exam.component";
import {ExamSubmitExamComponent} from "./exam-submit-exam/exam-submit-exam.component";
import {NotificationComponent} from "./notification/notification.component";
import {ExamSummaryComponent} from "./exam-summary/exam-summary.component";
import {MatIconModule} from "@angular/material/icon";
import {ExamDashboardComponent} from "./exam-dashboard/exam-dashboard.component";

@NgModule({
  declarations: [
    ExamTeacherComponent,
    ExamInstanceComponent,
    ExamDefinitionComponent,
    ExamStartExamComponent,
    ExamSubmitExamComponent,
    NotificationComponent,
    ExamStudentComponent,
    ExamSummaryComponent,
    ExamDashboardComponent
  ],
  imports: [
    CommonModule,
    SharedModule,
    RouterModule,
    ReactiveFormsModule,
    SharedModule,
    NgForOf,
    ReactiveFormsModule,
    NgForOf,
    ReactiveFormsModule,
    FormsModule,
    NgIf,
    MatIconModule,
  ],
  exports: [
    ExamTeacherComponent,
    ExamInstanceComponent,
    ExamDefinitionComponent,
    ExamStartExamComponent,
    ExamSubmitExamComponent,
    NotificationComponent,
    ExamStudentComponent,
    ExamSummaryComponent,
    ExamDashboardComponent
  ]
})
export class ExamModule { }
