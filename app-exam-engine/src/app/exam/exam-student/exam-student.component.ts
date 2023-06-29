import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { ExamService } from "../../services/exam.service";
import {ExamInstanceModel} from "../../models/exam-instance-model";
import {StorageService} from "../../services/storage.service";
import {AlertService} from "../../services/alert.service";
import {FirebaseService} from "../../services/firebase.service";

@Component({
  selector: 'app-exam-start',
  templateUrl: './exam-student.component.html',
  styleUrls: ['./exam-student.component.css']
})
export class ExamStudentComponent {
  userRole: any;
  userName: any;
  studentId: any;
  examInstances: ExamInstanceModel[]= [];

  public notification: string = "";
  public generatedLinkUrl: string = "";

  constructor(
    private router: Router,
    private examService: ExamService,
    private storageService: StorageService,
    private alertService: AlertService,
    private firebaseService: FirebaseService
  ) {}

  ngOnInit(): void {
    this.userName = this.storageService.getUserName();
    this.studentId = this.storageService.getUserEmail();
    this.userRole = this.storageService.getUserRole();
    this.examService.getExamInstancesByStudentId(this.studentId).subscribe({
      next: (exams) => this.examInstances = exams,
      error: (err) => this.alertService.show(err, 'alert-warning')
    });

    this.firebaseService.requestPermission(this.studentId);
    this.firebaseService.listenForMessages((payload: any) => {
      console.log('Received payload in component:', payload);
      this.notification = payload.data.message;
      this.generatedLinkUrl = payload.data.generatedLinkUrl;
    });
  }

  startExam(exam: ExamInstanceModel) {
      this.router.navigate(['/exam-status', exam.generatedLink.url]);
  }

}
