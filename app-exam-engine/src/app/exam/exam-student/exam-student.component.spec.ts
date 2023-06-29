import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ExamStudentComponent } from './exam-student.component';

describe('ExamStartComponent', () => {
  let component: ExamStudentComponent;
  let fixture: ComponentFixture<ExamStudentComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ExamStudentComponent]
    });
    fixture = TestBed.createComponent(ExamStudentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
