import { Component } from '@angular/core';
import {FormArray, FormControl, FormGroup, Validators} from "@angular/forms";
import {Router} from "@angular/router";
import {ExamService} from "../../services/exam.service";
import {GeneratedLinkModel} from "../../models/generated-link-model";
import {ExamInstanceModel} from "../../models/exam-instance-model";
import {ExamDefinitionModel} from "../../models/exam-definition-model";
import {UserModel} from "../../models/user-model";
import {StorageService} from "../../services/storage.service";
import {UserService} from "../../services/user.service";
import {AlertService} from "../../services/alert.service";
import { v4 as uuidv4 } from 'uuid';

// todo: check that from_Date is before to_Date!
@Component({
  selector: 'app-exam-instance',
  templateUrl: './exam-instance.component.html',
  styleUrls: ['./exam-instance.component.css']
})
export class ExamInstanceComponent {
  userRole: any;
  userName: any;
  userId: any;
  // @ts-ignore
  newInstanceForm: FormGroup;
  SelectedExamDefinitionId: number = 1;
  selectedStudents: string[] = [];
  allStudents: UserModel[] = [];
  examDefinitions: ExamDefinitionModel[] = [];

  constructor(
    private router: Router,
    private examService: ExamService,
    private userService: UserService,
    private storageService: StorageService,
    private alertService: AlertService,
  ) {}


  ngOnInit(): void {
    this.userRole = this.storageService.getUserRole();
    this.userName = this.storageService.getUserName();
    this.userId = this.storageService.getUserEmail();
    if (this.userRole==="student"){
      this.router.navigate(['/dashboard']);
    }
    this.newInstanceForm = new FormGroup({
      examDefinition: new FormControl('', Validators.required), // examDefinitionId
      durationInMinutes: new FormControl(''),
      generatedLink_from: new FormControl('', [Validators.required]), //, this.validateFromDate
      generatedLink_to: new FormControl('', [Validators.required]), //
      students: new FormArray([]),
    });

    this.userService.getAllStudents().subscribe(allStudents => {
      this.allStudents = allStudents;
    });
    this.examService.getExamDefinitions().subscribe(examDefinitions => {
      this.examDefinitions = examDefinitions;
    });

  }

  getCurrentDateTime() {
    return new Date().toISOString().slice(0, 16);
  }

  getMaxDateTime() {
    let selectedDateTimeStr = this.newInstanceForm.get('generatedLink_from')!.value ? this.newInstanceForm.get('generatedLink_from')!.value : new Date();
    let selectedDateTime = new Date(selectedDateTimeStr);
    let maxDate = new Date(selectedDateTime.getTime() + (24 * 60 * 60 * 1000 * 7));
    return maxDate.toISOString().slice(0, 16);
  }

  onCheckboxChange(student: UserModel) {
    const isSelected = this.selectedStudents.includes(student.email);

    if (isSelected) {
      this.removeStudent(student);
    } else {
      this.addStudent(student);
    }
  }

  addStudent(student: UserModel) {
    this.selectedStudents.push(student.email);
    const control = new FormControl(student);
    (this.newInstanceForm.get('students') as FormArray).push(control);
  }

  removeStudent(student: UserModel) {
    const index = this.selectedStudents.indexOf(student.email);
    if (index !== -1) {
      this.selectedStudents.splice(index, 1);
    }
    (this.newInstanceForm.get('students') as FormArray).removeAt(index);
  }

  get studentControls(): FormArray {
    return this.newInstanceForm.get('students') as FormArray;
  }

  onSubmit(): void {
    for (let selectedStudent of this.selectedStudents) {
      let generatedLink: GeneratedLinkModel = {
        scheduledTimeFrom: this.newInstanceForm.get('generatedLink_from')!.value,
        scheduledTimeTo: this.newInstanceForm.get('generatedLink_to')!.value,
        url: uuidv4(),
      }

      let newExamInstance = {
        id: null,
        examDefinitionId: this.newInstanceForm.get('examDefinition')?.value,
        durationInMinutes: this.newInstanceForm.get('durationInMinutes')!.value,
        generatedLink: generatedLink,
        assignedBy: this.userId,
        takenBy: String(selectedStudent),
        status: "assigned"
      };
      this.examService.addExamInstance(newExamInstance);
    }
  }

  onBack(): void {
    this.router.navigate(['/dashboard']);
  }

  onReset(): void {
    this.newInstanceForm.reset();
  }

}
