import { Component } from '@angular/core';
import { Router } from '@angular/router';
import {StorageService} from "../../services/storage.service";
import {AlertService} from "../../services/alert.service";

@Component({
  selector: 'app-exam-start',
  templateUrl: './exam-dashboard.component.html',
  styleUrls: ['./exam-dashboard.component.css']
})
export class ExamDashboardComponent {
  user: any;
  messages: string[] = [];

  constructor(private router: Router,
              private storageService: StorageService,
              private alertService: AlertService,
  ) {}

  ngOnInit(): void {

    let role = this.storageService.getUserRole();
    this.user = this.storageService.getUser();

    if (role === 'student') {
      this.router.navigateByUrl('/student');
    }
    else if (role === 'teacher') {
      this.router.navigateByUrl('/teacher');
    }
    else{
      this.alertService.show("User is not an teacher or student!" , 'alert-warning')
    }
  }

  get loggedIn(){
    return this.storageService.isLoggedIn();
  }
}
