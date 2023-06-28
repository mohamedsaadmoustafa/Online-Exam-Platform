import { animate, state, style, transition, trigger } from '@angular/animations';
import { Component, Input, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { QuestionModel } from 'src/app/models/question-model';
import { QuestionsService } from '../../services/questions.service';
import {AlertService} from "../../services/alert.service";
import {StorageService} from "../../services/storage.service";
import {AuthenticationService} from "../../services/authentication.service";

@Component({
  selector: 'app-questions-list',
  templateUrl: './questions-list.component.html',
  styleUrls: ['./questions-list.component.css'],
  animations: [
    trigger('questionItem',[
      state('in', style({opacity: 1})),
        state('delete', style({opacity: 0, transform: 'translateX(10px)'})),

      transition('void => *', [style({opacity: 0}), animate(1000)]),
      transition('in => void', animate(300, style({opacity: 0, transform: 'translateX(40px)'}))),
    ]),
  ]
})
export class QuestionsListComponent implements OnInit, OnDestroy {
  userRole: any;

  @Input()questionsCount: number;
  protected readonly Math = Math;
  questions: QuestionModel[] = [];
  questionState: string;
  // pagination variables
  currentPage = 1;
  readonly pageSize = 5;

  constructor(
    private readonly questionsService: QuestionsService,
    private readonly router: Router,
    private readonly route: ActivatedRoute,
    private alertService: AlertService,
    private storageService: StorageService,
    private authenticationService: AuthenticationService
  ) {}

  ngOnInit(): void {
    this.userRole = this.storageService.getUserRole();
    if (this.userRole==="student"){
      this.authenticationService.logout();
    }
    if ( !this.storageService.isLoggedIn()){
      this.router.navigate(['/login']);
    }

    this.loadQuestions(this.currentPage);
    this.loadQuestionsCount();
  }

  isLoggedIn(): boolean {
    return this.authenticationService.isLoggedIn()
  }

  loadQuestionsCount(): void {
    this.questionsService.getQuestionsCount().subscribe(
      count => this.questionsCount = count
    );
  }

  loadQuestions(currentPage): void {
    this.questionsService.loadQuestions(currentPage).subscribe(
      questions => {
        this.questions = questions;
        this.questionState = 'in';
        this.currentPage = currentPage;
      },
      error =>  this.alertService.show(error, 'alert-warning')

  );
    window.scrollTo(0, 0); // scroll to top of page
  }

  goToQuestionDetails(index: number, event: Event): void {
    event.stopPropagation();
    this.router.navigate(['question-details', index], {
      relativeTo: this.route,
    });
  }

  removeQuestion(index: number, event: Event) {
    event.stopPropagation();

    this.questionsService.deleteQuestion(index);
    this.questions.splice(index, 1);
    this.questionState = 'delete';
    this.questionsCount--;
  }

  ngOnDestroy(): void {}
}
