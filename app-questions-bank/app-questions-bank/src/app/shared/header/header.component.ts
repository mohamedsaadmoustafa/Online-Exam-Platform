import { Component, OnInit } from '@angular/core';
import {AuthenticationService} from "../../services/authentication.service";
import {StorageService} from "../../services/storage.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit {
  constructor(
    private authenticationService: AuthenticationService,
    private storageService: StorageService,
    private router: Router,
  ) { }

  ngOnInit(): void {
    if ( !this.storageService.isLoggedIn()){
      this.router.navigate(['/login']);
    }
  }

  isLoggedIn(): boolean {
    return this.authenticationService.isLoggedIn()
  }

  getName(): string {
    return this.storageService.getUser().name;
  }

  logout(): void {
    this.authenticationService.logout();
    this.router.navigate(['/login']);
  }
}
