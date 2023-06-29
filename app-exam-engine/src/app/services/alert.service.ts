import { Injectable } from '@angular/core';
declare var $: any;

@Injectable({
  providedIn: 'root'
})
export class AlertService {
  show(message: string, type: string) {
    const alert = $(`
      <div class="alert alert-dismissible fade show ${type}" role="alert">
        <span>${message}</span>
        <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
      </div>
    `);

    $('.alert').prepend(alert);

    setTimeout(() => {
      alert.alert('close');
    }, 2000);
  }
}
