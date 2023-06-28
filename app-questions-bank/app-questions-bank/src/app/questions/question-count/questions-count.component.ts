import { Component, Input, OnInit } from '@angular/core';

@Component({
  selector: 'app-questions-count',
  templateUrl: './questions-count.component.html',
  styleUrls: ['./questions-count.component.css']
})

export class QuestionsCountComponent implements OnInit {
  @Input()numberOfQuestions: number;
  constructor() { }
  ngOnInit(): void {

  }
}
