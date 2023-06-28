import { Component, OnInit } from '@angular/core';
import {FormArray, FormControl, FormGroup, Validators} from '@angular/forms';
import { Router, ActivatedRoute } from '@angular/router';
import { v4 as uuidv4 } from 'uuid';
import { QuestionsService } from '../../services/questions.service';
import {AnswerModel} from "../../models/answer-model";
import {environment} from "../../../environments/environment";
import { DateTime } from "luxon";
import {StorageService} from "../../services/storage.service";
import {AuthenticationService} from "../../services/authentication.service";

@Component({
  selector: 'app-question-edit',
  templateUrl: './question-edit.component.html',
  styleUrls: ['./question-edit.component.css'],
})
export class QuestionEditComponent implements OnInit {
  userName: any;
  userRole: any;

  levels = environment.levels;
  categories = environment.categoties;
  subCategories = environment.subcategories;
  editQuestionForm: FormGroup;
  answersFormArray;
  questionIndex;
  questionId;

  constructor(
    private router: Router,
    private route: ActivatedRoute,
    private questionsService: QuestionsService,
    private storageService: StorageService,
    private authenticationService: AuthenticationService,
  ) {}

  ngOnInit(): void {
    this.userName = this.storageService.getUserName();
    this.userRole = this.storageService.getUserRole();
    if (this.userRole==="student"){
      this.authenticationService.logout();
    }
    if ( !this.storageService.isLoggedIn()){
      this.router.navigate(['/login']);
    }

    let question;
    if (this.route.snapshot.params['id']) {
      // edit existing question
      this.questionIndex = this.route.snapshot.params['id'];
      question = this.questionsService.getQuestion(this.questionIndex);
      this.questionId = question.id; // save the question id}
    } else {
      // add new question
      question = {
        id: null,
        name: '',
        levelId: '',
        category: '',
        subCategory: '',
        mark: '',
        expectedTime: '',
        createdBy: '',
        answers: [],
        correctAnswerIds: []
      }
    }

    this.editQuestionForm = new FormGroup({
      name: new FormControl(question.name, [Validators.required]),
      levelId: new FormControl(question.levelId, [Validators.required]),
      category: new FormControl(question.category, [Validators.required]),
      subCategory: new FormControl(question.subCategory),
      mark: new FormControl(question.mark, [Validators.required]),
      expectedTime: new FormControl(question.expectedTime, [Validators.required]),
      answers: new FormArray(
        question.answers.map(answer => this.initAnswerFormGroup(answer)),
        [Validators.required]
      ),
      correctAnswerIds: new FormArray(
        question.correctAnswerIds.map(id => new FormControl(id))
      )
    });
    this.answersFormArray = this.editQuestionForm.get('answers') as FormArray;
  }

  initAnswerFormGroup(answer: AnswerModel) {
    return new FormGroup({
      id: new FormControl(answer.id),
      answerName: new FormControl(answer.answerName, [Validators.required]),
      answerDescription: new FormControl(answer.answerDescription)
    });
  }

  // Add answers from html form
  addAnswerFormGroup() {
    this.answersFormArray = this.editQuestionForm.get('answers') as FormArray;
    const newAnswer: AnswerModel = {
      id: uuidv4(),
      answerName: '',
      answerDescription: ''
    };
    this.answersFormArray.push(this.initAnswerFormGroup(newAnswer));
  }
  removeAnswerFormGroup(index) {
    this.answersFormArray.removeAt(index);
    // if removed answer were checked, delete its id from correct answers id
    let correctAnswerIds = this.editQuestionForm.get('correctAnswerIds') as FormArray;
    let answerFormGroup = this.answersFormArray.controls[index] as FormGroup;
    let answerId = answerFormGroup.get('id').value;
    let indexToRemove = correctAnswerIds.controls.findIndex(control => control.value === answerId);
    if (indexToRemove !== -1) {
      correctAnswerIds.removeAt(indexToRemove);
    }
  }

  get answerControls() {
    return (this.editQuestionForm.get('answers') as FormArray).controls;
  }

  checkIndexInList(index: number, list: number[]): boolean {
    let answerFormGroup = this.answersFormArray.controls[index] as FormGroup;
    let answerId = answerFormGroup.get('id').value;
    return list.includes(answerId);
  }

  onAnswerChecked(checked: boolean, index: number) {
    let correctAnswerIdsFormArray = this.editQuestionForm.get('correctAnswerIds') as FormArray;
    let answerFormGroup = this.answersFormArray.controls[index] as FormGroup;
    let answerId = answerFormGroup.get('id').value;
    if (checked) {
      correctAnswerIdsFormArray.push(new FormControl(answerId));
    } else {
      let indexToRemove = correctAnswerIdsFormArray.value.findIndex(x => x === index.toString());
      correctAnswerIdsFormArray.removeAt(indexToRemove);
    }
  }

  onSubmit() {
    let editedQuestion = {
      id: this.questionId,
      name: this.editQuestionForm.get('name').value,
      levelId: this.editQuestionForm.get('levelId').value,
      category: this.editQuestionForm.get('category').value,
      subCategory: this.editQuestionForm.get('subCategory').value,
      mark: this.editQuestionForm.get('mark').value,
      expectedTime: this.editQuestionForm.get('expectedTime').value,
      createdBy: this.userName,
      // createdAt: DateTime.local().toISO(),
      answers: this.editQuestionForm.get('answers').value,
      correctAnswerIds: this.editQuestionForm.get('correctAnswerIds').value
    };

    this.questionsService.updateQuestion(editedQuestion);
    this.onBack();
  }


  onBack() {
    this.router.navigate(['/questions']);
  }

  onReset() {
    let question = this.questionsService.getQuestion(this.questionIndex);
    this.editQuestionForm.patchValue({
      name: question.name,
      levelId: question.levelId,
      category: question.category,
      subCategory: question.subCategory,
      mark: question.mark,
      expectedTime: question.expectedTime,
      answers: question.answers.map(answer => ({
        id: answer.id,
        answerName: answer.answerName,
        answerDescription: answer.answerDescription
      })),
      correctAnswerIds: question.correctAnswerIds
    });

  }
}
