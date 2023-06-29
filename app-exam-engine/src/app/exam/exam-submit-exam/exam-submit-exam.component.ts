import { Component } from '@angular/core';
import {Router} from '@angular/router';
import {StorageService} from "../../services/storage.service";

@Component({
  selector: 'app-exam-submit-exam',
  templateUrl: './exam-submit-exam.component.html',
  styleUrls: ['./exam-submit-exam.component.css']
})
export class ExamSubmitExamComponent {
  userRole: any;
  userName: any;

  constructor(
    private router: Router,
    private storageService: StorageService
  ) {}

  ngOnInit() {
    this.userName = this.storageService.getUserName();
    this.userRole = this.storageService.getUserRole();
  }

  onBack(){
    this.router.navigateByUrl('/');
  }
}
