import {Component, NgZone, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {ExamService} from "../../services/exam.service";
import {QuestionModel} from "../../models/question-model";
import {ExamInstanceQuestionModel} from "../../models/exam-instance-question-model";
import {StorageService} from "../../services/storage.service";
import {AlertService} from "../../services/alert.service";
import {LocalDateTime} from "@js-joda/core";
import {DatePipe} from "@angular/common";

@Component({
  selector: 'app-exam-start-exam',
  templateUrl: './exam-start-exam.component.html',
  styleUrls: ['./exam-start-exam.component.css']
})
export class ExamStartExamComponent implements OnInit {
  count = 0;
  stopTimer: boolean = false;

  userRole: any;
  generatedLinkUrl: string | null = '';

  questions: QuestionModel[] = [];
  answeredQuestions: ExamInstanceQuestionModel[] = [];
  isAnswerSelected = false;
  selectedAnswers: boolean[] = [];
  currentQuestionIndex = 0;

  startTime: Date = new Date();
  displayTime:Date = new Date();
  answerTime:Date = new Date();

  keepInterval: boolean = true;

  constructor(
    private router: Router,
    private route: ActivatedRoute,
    private examService: ExamService,
    private storageService: StorageService,
    private alertService: AlertService,
    private ngZone: NgZone
  ) {}

  ngOnInit() {
    this.userRole = this.storageService.getUserRole();
    this.generatedLinkUrl = this.route.snapshot.paramMap.get('generatedLinkUrl');
    // Perform time-consuming operation outside of Angular's change detection cycle
    if (this.stopTimer){
      this.ngZone.runOutsideAngular(() => {
        setInterval(() => {
          this.count++;
        }, 1000);
      });
    }
  }

  ngAfterViewInit(): void {
    this.examService.getExamQuestions(this.generatedLinkUrl).subscribe(questions => {
      if (questions.length === 0) {
        this.alertService.show('Exam has no questions.', 'alert-warning');
        this.router.navigateByUrl('/');
      }
      this.questions = questions;
    });

    setInterval(() => {
      this.checkQuestionTimeLimit();
    }, 1000);
  }

  get currentQuestion(){
    return this.questions[this.currentQuestionIndex];
  }

  get elapsedTime(){
    return Math.floor((new Date().getTime() - this.displayTime.getTime()) / 1000);
  }

  checkQuestionTimeLimit() {
    if (this.elapsedTime >= this.currentQuestion!.expectedTime) {
      this.nextQuestion();
    }
  }

  get getRemainingTime(): number {
    return Math.max(0, this.currentQuestion!.expectedTime - this.elapsedTime);
  }

  convertDateToLocalDateTime(date: Date): LocalDateTime {
    let format = 'yyyy-MM-ddTHH:mm:ss.SSS';
    const datePipe = new DatePipe('en-US');
    const formattedDate = datePipe.transform(date, format);
    return LocalDateTime.parse(<string>formattedDate);
  }

  nextQuestion() {
    // reset timer
    this.answerTime = this.displayTime;
    this.displayTime = new Date();

    if (0 <= this.currentQuestionIndex && this.currentQuestionIndex < this.questions!.length ) {
      let selectedAnswerIds = this.currentQuestion.answers
        .filter((answer, i) => this.selectedAnswers[i])
        .map(answer => answer.id);

      if (Object.keys(selectedAnswerIds).length === 0){
        selectedAnswerIds = ['-1'];
      }

      let answeredQuestion:ExamInstanceQuestionModel = {
        questionId: this.currentQuestion.id,
        selectedAnswerIds: selectedAnswerIds,
        displayTime: this.convertDateToLocalDateTime(this.displayTime),
        answerTime: this.convertDateToLocalDateTime(this.answerTime)
      };
      this.addAnsweredQuestion(answeredQuestion);
      this.currentQuestionIndex++;
      this.selectedAnswers = [];
    }

    if (this.currentQuestionIndex === this.questions!.length){
      this.stopTimer = true;
      this.onSubmit();
    }
  }

  addAnsweredQuestion(answeredQuestion: ExamInstanceQuestionModel){
    this.answeredQuestions.push(answeredQuestion);
  }

  onSubmit(): void {
    this.keepInterval = false;
    let examInstance = {
      startTime: this.convertDateToLocalDateTime(this.startTime),
      endTime: this.convertDateToLocalDateTime(new Date()),
      questions: this.answeredQuestions
    }
    this.examService.patchExamQuestions(this.generatedLinkUrl, examInstance);
    this.router.navigateByUrl('/'); // exam-submit
  }
}
