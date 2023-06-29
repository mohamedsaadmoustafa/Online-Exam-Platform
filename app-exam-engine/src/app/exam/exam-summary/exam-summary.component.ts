import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { ExamService } from "../../services/exam.service";
import {StorageService} from "../../services/storage.service";
import {AlertService} from "../../services/alert.service";
import {ExamSummaryModel} from "../../models/exam.summary.model";

@Component({
  selector: 'app-exam-start',
  templateUrl: './exam-summary.component.html',
  styleUrls: ['./exam-summary.component.css']
})
export class ExamSummaryComponent {
  userRole: any;
  userName: any;
  studentId: any;
  examSummaries: ExamSummaryModel[]= [];
  currentPage: number = 1;
  count: number = 0;
  readonly pageSize = 5;

  constructor(
    private router: Router,
    private examService: ExamService,
    private storageService: StorageService,
    private alertService: AlertService
  ) {}

  ngOnInit(): void {
    this.userName = this.storageService.getUserName();
    this.studentId = this.storageService.getUserEmail();
    this.userRole = this.storageService.getUserRole();

    this.loadExamSummaryByStudentId(this.currentPage);
    this.loadExamSummariesCount();
  }

  loadExamSummaryByStudentId(currentPage: number): void {
    this.examService.getExamSummaryByStudentId(this.studentId, currentPage).subscribe(
      exams => {
        this.examSummaries = exams;
        this.currentPage = currentPage;
      },
      err => this.alertService.show(err, 'alert-warning')
    );
    window.scrollTo(0, 0); // scroll to top of page
  }

  loadExamSummariesCount(): void {
    this.examService.getExamSummariesCount(this.studentId).subscribe(
      count => this.count = count
    );
  }

  protected readonly Math = Math;
}
