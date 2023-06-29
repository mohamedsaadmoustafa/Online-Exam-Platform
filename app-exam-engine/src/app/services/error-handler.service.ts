import { Injectable } from '@angular/core';
import {AlertService} from "./alert.service";
import {throwError} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class ErrorHandlerService {

  constructor(
    private alertService: AlertService,
  ) {}

  public errorHandler(error: any): void {
    if (error.status === 400) {
      this.alertService.show('Bad Request: The request was malformed or missing required parameters.', 'alert-warning');
    } else if (error.status === 401) {
      this.alertService.show('Unauthorized: The user is not authenticated and needs to provide valid credentials.', 'alert-warning');
    } else if (error.status === 403) {
      this.alertService.show('Forbidden: The user is authenticated but does not have sufficient privileges to access the requested resource.', 'alert-warning');
    } else if (error.status === 404) {
      this.alertService.show('Not Found: The requested resource (such as an API endpoint) was not found.', 'alert-warning');
    } else if (error.status === 500) {
      this.alertService.show('Internal Server Error: There was an error on the server side while processing the request.', 'alert-warning');
    } else {
      this.alertService.show(error.message, 'alert-warning');
    }
  }
}
