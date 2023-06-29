import { Component, Input } from '@angular/core';

// <app-notification type="success" message="Your action was successful"></app-notification>
// <app-notification type="error" message="An error occurred"></app-notification>

@Component({
  selector: 'app-notification',
  template: `
    <div class="notification" [class.success]="type === 'success'" [class.error]="type === 'error'">
      <i class="fas fa-check-circle"></i> <span>{{ message }} </span>

      <ng-container *ngIf="url">
        <a routerLink="/exam-status/{{ url }}">here</a>
      </ng-container>
    </div>
  `,
  styleUrls: ['./notification.component.css']
})
export class NotificationComponent {
  // @ts-ignore
  @Input() type: string;
  // @ts-ignore
  @Input() message: string;
  // @ts-ignore
  @Input() url: string;
}
