import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import {Observable, of} from 'rxjs';
import { catchError, tap } from 'rxjs/operators';
import { environment } from '../../environments/environment';
import { ExamDefinitionModel } from '../models/exam-definition-model';
import { ExamInstanceModel } from '../models/exam-instance-model';
import { QuestionModel } from "../models/question-model";
import {ExamInstanceQuestionModel} from "../models/exam-instance-question-model";
import {AlertService} from "./alert.service";
import {Router} from "@angular/router";
import {GeneratedLinkModel} from "../models/generated-link-model";
import { throwError } from 'rxjs';
import {LocalDateTime} from "@js-joda/core";
import {ErrorHandlerService} from "./error-handler.service";
import {ExamSummaryModel} from "../models/exam.summary.model";


const headers = new HttpHeaders().set('Content-Type', 'application/json');

@Injectable({
  providedIn: 'root'
})
export class ExamService {
  private questionBankApiUrl = environment.questionBankApiUrl;
  private examEngineApiUrl = environment.examEngineApiUrl;
  private authenticationApiUrl = environment.authenticationApiUrl;
  questions: QuestionModel[] = []

  constructor(
    private http: HttpClient,
    private alertService: AlertService,
    private router: Router,
    private errorHandlerService: ErrorHandlerService,
  ) {}

  public getQuestionsByCategory(category: string): Observable<QuestionModel[]> {
    if (!category) {
      this.alertService.show('Category cannot be null or empty', 'alert-warning');
    }
    let url = `${this.questionBankApiUrl}filter?category=${category}`;
    return this.http.get<QuestionModel[]>(url).pipe(
      tap((questions) => {
        if (questions.length === 0) {
          this.alertService.show('No Questions found in this category.', 'alert-warning');
        }
        this.questions = questions;
      })
    );
  }

  public getExamInstancesByStudentId(studentId: string):Observable<ExamInstanceModel[]> {
    if (!studentId) {
      this.alertService.show('StudentId cannot be null or empty', 'alert-warning');
    }
    let url = `${this.examEngineApiUrl}students/${studentId}/exam-instances`;
    return this.http.get<ExamInstanceModel[]>(url);
  }

  public getExamDefinitions(): Observable<ExamDefinitionModel[]> {
    let url = `${this.examEngineApiUrl}exam-definition`;
    return this.http.get<ExamDefinitionModel[]>(url);
  }


  getExamQuestions(generatedLinkUrl: string | null): Observable<QuestionModel[]> {
    if (!generatedLinkUrl) {
      this.alertService.show('Generated Link Url cannot be null or empty', 'alert-warning');
      this.router.navigate(['/']);
    }
    const url = `${this.examEngineApiUrl}exam-gateway/${generatedLinkUrl}`;
    return this.http.get<QuestionModel[]>(url, { headers }).pipe(
      catchError((error) => {
        this.router.navigate(['/']);
        this.errorHandlerService.errorHandler(error);
        return throwError('Failed to fetch exam questions.');
      })
    );
  }

  public addExamDefinition(exam: ExamDefinitionModel) {
    if (!exam) {
      this.alertService.show('Exam Definition cannot be null or empty', 'alert-warning');
    }
    let url = `${this.examEngineApiUrl}exam-definition`;
    return this.http.post(url , exam, { responseType: 'text' }).pipe(
      tap((response) => {
        this.alertService.show('New Exam Definition Added', 'alert-primary');
        this.router.navigate(['/dashboard']);
      }),
      catchError((error) => {
        this.errorHandlerService.errorHandler(error);
        return of(null);
      }),
    )
      .subscribe();
  }

  public addExamInstance(exam: {
    generatedLink: GeneratedLinkModel;
    assignedBy: any;
    takenBy: string;
    durationInMinutes: any;
    id: null;
    examDefinitionId: any;
    status: string
  }) {
    if (!exam) {
      this.alertService.show('Exam Instance cannot be null or empty', 'alert-warning');
    }
    let url = `${this.examEngineApiUrl}assign-exam-to-student`;
    this.http.post(url , exam, {
      headers: new HttpHeaders({
        'Content-Type': 'application/json',
        'Access-Control-Allow-Origin': '*',
        'Access-Control-Allow-Methods': 'GET,POST,PUT,DELETE,OPTIONS',
        'Access-Control-Allow-Headers': 'content-type,authorization'
      })
    }).pipe(
      tap((response) => {
        this.alertService.show('New Exam Instance Added', 'alert-primary');
        this.router.navigate(['/dashboard']);
      }),
      catchError((error) => {
        this.errorHandlerService.errorHandler(error);
        return of(null);
      }),
    )
      .subscribe();
  }

  patchExamQuestions(generatedLinkUrl: string | null, examInstance: {
    questions: ExamInstanceQuestionModel[];
    startTime: LocalDateTime;
    endTime: LocalDateTime
  }){
    if (!generatedLinkUrl) {
      this.alertService.show('Generated Link Url cannot be null or empty', 'alert-warning');
    }
    let url = `${this.examEngineApiUrl}exam-gateway/${generatedLinkUrl}`;
    return this.http.patch(url, examInstance, { responseType: 'text' }).pipe(
      tap((response) => {
        this.alertService.show('Submit Exam!', 'alert-primary');
        this.router.navigate(['/dashboard']);
      }),
      catchError((error) => {
        this.alertService.show('failed to submit exam: ' + error, 'alert-error');
        this.errorHandlerService.errorHandler(error);
        return of(null);
      }),
    )
      .subscribe();
  }

  getExamSummaryByStudentId(studentId: string, pageNumber: number):Observable<ExamSummaryModel[]> {
    if (!studentId) {
      this.alertService.show('StudentId cannot be null or empty', 'alert-warning');
    }
    let url = `${this.authenticationApiUrl}user/${studentId}/exam-summary?pageNo=${pageNumber}`;
    return this.http.get<ExamSummaryModel[]>(url);
  }

  getExamSummariesCount(studentId: string) {
    if (!studentId) {
      this.alertService.show('StudentId cannot be null or empty', 'alert-warning');
    }
    let url = `${this.authenticationApiUrl}user/${studentId}/exam-summary/count`;
    return this.http.get<number>(url);
  }
}
