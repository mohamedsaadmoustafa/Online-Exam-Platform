import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {ExamTeacherComponent} from "./exam/exam-teacher/exam-teacher.component";
import {ExamStartExamComponent} from "./exam/exam-start-exam/exam-start-exam.component";
import {ExamInstanceComponent} from "./exam/exam-instance/exam-instance.component";
import {ExamDefinitionComponent} from "./exam/exam-definition/exam-definition.component";
import {NotificationComponent} from "./exam/notification/notification.component";
import {ExamStudentComponent} from "./exam/exam-student/exam-student.component";
import {ExamSubmitExamComponent} from "./exam/exam-submit-exam/exam-submit-exam.component";
import {RegisterComponent} from "./user/user-register/register.component";
import {LoginComponent} from "./user/user-login/login.component";
import {AuthGuard} from "./services/auth.guard";
import {ExamSummaryComponent} from "./exam/exam-summary/exam-summary.component";
import {ExamDashboardComponent} from "./exam/exam-dashboard/exam-dashboard.component";


const routes: Routes = [
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'dashboard', component: ExamDashboardComponent },//, canActivate: [AuthGuard], data:{'role': ['student', 'teacher']} },
  { path: 'teacher', component: ExamTeacherComponent },//, canActivate: [AuthGuard], data:{'role': 'teacher'} },
  { path: 'student', component: ExamStudentComponent },//, canActivate: [AuthGuard], data:{'role': ['student']} },
  { path: 'exam-definition', component: ExamDefinitionComponent },//, canActivate: [AuthGuard], data:{'role': ['teacher']} },
  { path: 'exam-instance', component: ExamInstanceComponent },//, canActivate: [AuthGuard], data:{'role': ['teacher']} },
  { path: 'exam-submit', component: ExamSubmitExamComponent },//, canActivate: [AuthGuard], data:{'role': ['student']} },
  { path: 'exam-summary', component: ExamSummaryComponent },//, canActivate: [AuthGuard], data:{'role': ['student']} },
  { path: 'exam-status/:generatedLinkUrl', component: ExamStartExamComponent },//, canActivate: [AuthGuard], data:{'role': ['student']} },

  { path: '', pathMatch: 'full', redirectTo: '/dashboard' },
  { path: '**', redirectTo: '/dashboard' },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
