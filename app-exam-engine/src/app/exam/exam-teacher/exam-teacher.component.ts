import { Component } from '@angular/core';
import { Router } from '@angular/router';
import {StorageService} from "../../services/storage.service";

@Component({
  selector: 'app-exam-start',
  templateUrl: './exam-teacher.component.html',
  styleUrls: ['./exam-teacher.component.css']
})
export class ExamTeacherComponent {
  userName: any;
  userRole: any;

  constructor(
    private router: Router,
    private storageService: StorageService
  ) {}

  ngOnInit() {
    this.userName = this.storageService.getUserName();
    this.userRole = this.storageService.getUserRole();
    if (this.userRole==="student"){
      this.router.navigate(['/dashboard']);
    }
  }

  navigateToExamDefinition() {
    this.router.navigateByUrl('/exam-definition');
  }

  navigateToExamInstance() {
    this.router.navigateByUrl('/exam-instance');
  }

  startExam() {
    // Todo: Navigate to exam form route for student to generate exam link
    this.router.navigateByUrl('/exam-form');
  }
}
