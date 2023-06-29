import { FormControl, ValidationErrors } from '@angular/forms';

export class DateValidator {

  static validateFromDate(control: FormControl): ValidationErrors | null {
    let today : Date = new Date();

    if (new Date(control.value) > today)
      return { "LessThanToday": true };

    return null;
  }
}
