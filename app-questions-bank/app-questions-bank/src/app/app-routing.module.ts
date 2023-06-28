import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { QuestionDetailsComponent } from './questions/question-details/question-details.component';
import { QuestionEditComponent } from './questions/question-edit/question-edit.component';
import { QuestionsListComponent } from './questions/questions-list/questions-list.component';
import {LoginComponent} from "./user/user-login/login.component";
import {AuthGuard} from "./services/auth.guard";

const routes: Routes = [
  { path: '', pathMatch: 'full', redirectTo: '/questions' },
  { path: 'login', component: LoginComponent },
  {
    path: 'questions',
    component: QuestionsListComponent,
    children: [
      { path: 'question-details/:id', component: QuestionDetailsComponent },
    ],
    canActivate: [AuthGuard],
    data:{'role': ['teacher']}
  },
  { path: 'question-edit/:id', component: QuestionEditComponent, canActivate: [AuthGuard], data:{'role': ['teacher']} },
  { path: 'question-add', component: QuestionEditComponent, canActivate: [AuthGuard] , data:{'role': ['teacher']} },
  { path: '**', redirectTo: '/questions' },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
