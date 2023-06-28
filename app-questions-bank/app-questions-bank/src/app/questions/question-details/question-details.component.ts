import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Params, Router } from '@angular/router';
import { QuestionsService } from '../../services/questions.service';
import {AlertService} from "../../services/alert.service";

@Component({
  selector: 'app-question-details',
  templateUrl: './question-details.component.html',
  styleUrls: ['./question-details.component.css']
})
export class QuestionDetailsComponent implements OnInit {
  index:number;
  questionDetails;

  constructor(
    private questionsService: QuestionsService,
    private route: ActivatedRoute,
    private router: Router,
    private alertService: AlertService
  ) {}

  ngOnInit(): void {
    this.loadQuestion();
  }

  loadQuestion(){
    this.route.params.subscribe(
      (params: Params )=>{
        this.index = +params ['id'];
        this.questionDetails = this.questionsService.getQuestion(this.index);
      }
     )
  }

  removeAnswer(answer_index){
    // ToDo: if correctAnswersId contains ONLY one answer then raise an error / warning
    let questionId = this.questionDetails.id;
    let answerId = this.questionDetails.answers[answer_index].id;

    this.questionsService.deleteAnswer(questionId, answerId);
    this.questionDetails.answers.splice(answer_index, 1);

  }
}
