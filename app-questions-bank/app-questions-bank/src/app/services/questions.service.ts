import { Injectable } from '@angular/core';
import {HttpClient, HttpErrorResponse, HttpHeaders} from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError, tap } from 'rxjs/operators';
import { environment } from '../../environments/environment';
import { QuestionModel } from '../models/question-model';
import {AlertService} from "./alert.service";
import {ErrorHandlerService} from "./error-handler.service";

const headers = new HttpHeaders({
  'Content-Type': 'application/json',
  'Access-Control-Allow-Origin': '*'
});

@Injectable({
  providedIn: 'root'
})
export class QuestionsService {
  private apiUrl = environment.questionBankApiUrl;
  questions: QuestionModel[] = [];

  constructor(private http: HttpClient,
              private alertService: AlertService,
              private errorHandlerService: ErrorHandlerService,
  ) {}

  public loadQuestions(pageNumber): Observable<QuestionModel[]> {
    const url = `${this.apiUrl}?pageNo=${pageNumber}`;
    return this.http.get<QuestionModel[]>(url).pipe(
      tap((questions) => {
        this.questions = questions;
      })
    );
  }

  public getQuestion(index: number): QuestionModel {
    if (index < 0 || index >= this.questions.length){
      throw new Error('Invalid question index');
    }
    return this.questions[index];
  }

  public addQuestion(question: QuestionModel) {
    if (!question) {
      this.alertService.show('Invalid argument: question cannot be null or empty', 'alert-warning')
      throw new Error('Invalid argument: question cannot be null or empty');
    }
    this.http.post(this.apiUrl, question, { headers }).subscribe(
      response => {
        this.alertService.show("New question added.", 'alert-success');
        this.questions.push(question)
      },
      error => this.alertService.show(error, 'alert-warning')
    );
  }

  public updateQuestion(updatedQuestion: QuestionModel) {
    if (!updatedQuestion) {
      this.alertService.show('Invalid argument: question cannot be null or empty', 'alert-warning')
      throw new Error('Invalid argument: updatedQuestion cannot be null or empty');
    }
    this.http.post(this.apiUrl, updatedQuestion, { headers }).subscribe(
      response => this.alertService.show("Update Question successfully", 'alert-success'),
      error => this.alertService.show(error, 'alert-warning')
    );
  }

  public deleteQuestion(index: number){
    const url = `${this.apiUrl}delete/${this.questions[index].id}`;
    this.http.delete(url, { headers }).subscribe(
      response => {
        this.questions.splice(index, 1);
        this.alertService.show("Delete Question successfully", 'alert-success')
      },
      error => this.alertService.show('error', 'alert-warning')
    );
  }

  public deleteAnswer(questionId: number, answerId: string){
    const url = `${this.apiUrl}delete/${questionId}/answer/${answerId}`;
    this.http.delete(url, { headers }).subscribe(
      response => {
        this.alertService.show("Delete Answer successfully", 'alert-success');
      },
      error => this.alertService.show(error, 'alert-warning')
    );
  }

  public getQuestionsCount() {
    const url = `${this.apiUrl}count`;
    return this.http.get<number>(url);
  }

}
