import {Component, Input, OnInit} from '@angular/core';
import {StorageService} from "../../services/storage.service";
import {AuthenticationService} from "../../services/authentication.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit {
  @Input() title: string = "Exam Engine";
  constructor(
    private authenticationService: AuthenticationService,
    private storageService: StorageService,
    private router: Router,
  ) { }
  ngOnInit(): void {
  }
  isLoggedIn(): boolean {
    return this.authenticationService.isLoggedIn()
  }

  getName(): string {
    return this.storageService.getUser().name;
  }

  logout(): void {
    this.authenticationService.logout();
    this.router.navigate(['/register']);
  }
}
