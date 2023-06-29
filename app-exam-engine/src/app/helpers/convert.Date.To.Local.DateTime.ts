import { DatePipe } from '@angular/common';
import { LocalDateTime } from '@js-joda/core';

export function  convertDateToLocalDateTime(date: Date): LocalDateTime {
  let format = 'yyyy-MM-ddTHH:mm:ss.SSS';
  const datePipe = new DatePipe('en-US');
  const formattedDate = datePipe.transform(date, format);
  return LocalDateTime.parse(<string>formattedDate);
}
