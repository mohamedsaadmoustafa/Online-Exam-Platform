import { Component, OnInit } from '@angular/core';
import {FormArray, FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';
import { ExamDefinitionModel } from '../../models/exam-definition-model';
import { environment } from '../../../environments/environment';
import { Router } from '@angular/router';
import { ExamService } from '../../services/exam.service';
import { QuestionModel } from '../../models/question-model';
import { map } from 'rxjs/operators';
import {StorageService} from "../../services/storage.service";
import {AlertService} from "../../services/alert.service";

@Component({
  selector: 'app-exam-definition',
  templateUrl: './exam-definition.component.html',
  styleUrls: ['./exam-definition.component.css']
})
export class ExamDefinitionComponent implements OnInit {
  userRole: any;
  userId: any;
  categories = environment.categories;
  newDefinitionForm: FormGroup = new FormGroup({
    name: new FormControl('', Validators.required),
    category: new FormControl('', Validators.required),
    passingScore: new FormControl('', Validators.required),
    questions: new FormArray([]),
  });
  selectedQuestions: number[] = [];
  selectedQuestionIds: string[] = [];

  constructor(
    private router: Router,
    private examService: ExamService,
    private formBuilder: FormBuilder,
    private storageService: StorageService,
    private alertService: AlertService,
  ) {}

  ngOnInit(): void {
    this.userRole = this.storageService.getUserRole();
    if (this.userRole==="student"){
      this.router.navigate(['/dashboard']);
    }
    this.userId = this.storageService.getUserEmail();
    this.newDefinitionForm = new FormGroup({
      name: new FormControl('', Validators.required),
      category: new FormControl('', Validators.required),
      passingScore: new FormControl('', Validators.required),
      questions: new FormArray([]),
    });
  }

  onCategorySelect(): void {
    let category = this.newDefinitionForm.get('category')!.value;
    this.examService.getQuestionsByCategory(category).pipe(
      map((questionList: QuestionModel[]) => questionList.map(question =>
        this.formBuilder.group({
          id: question.id,
          name: question.name,
          checked: false
        })
      ))
    ).subscribe(
      (questionControls) => {
        this.resetSelectedQuestions();
        const questionsArray = this.newDefinitionForm.get('questions') as FormArray;
        questionControls.forEach(control => questionsArray.push(control));
      }
    );
    this.resetSelectedQuestions(); // add this line to reset selected questions
  }

  resetSelectedQuestions(): void {
    this.selectedQuestions = [];
    this.selectedQuestionIds = [];
    const questionsArray = this.newDefinitionForm.get('questions') as FormArray;
    questionsArray.clear();
  }

  get questionControls(): FormArray {
    return this.newDefinitionForm.get('questions') as FormArray;
  }

  onCheckboxChange(event: any, index: number) {
    const isChecked = event.target.checked;
    if (isChecked) {
      const questionId = this.questionControls.at(index).value.id;
      this.selectedQuestionIds.push(questionId);
    } else {
      const questionIdIndex = this.selectedQuestionIds.indexOf(this.questionControls.at(index).value.id);
      this.selectedQuestionIds.splice(questionIdIndex, 1);
    }
  }

  onSubmit(): void {
    const newExamDefinition: ExamDefinitionModel = {
      id: null,
      name: this.newDefinitionForm.get('name')!.value,
      category: this.newDefinitionForm.get('category')!.value,
      passingScore: this.newDefinitionForm.get('passingScore')!.value,
      createdBy: this.userId,
      questions: this.selectedQuestionIds,
    };
    this.examService.addExamDefinition(newExamDefinition);
  }

  onBack(): void {
    this.router.navigate(['/dashboard']);
  }

  onReset(): void {
    this.newDefinitionForm.reset();
  }
}
