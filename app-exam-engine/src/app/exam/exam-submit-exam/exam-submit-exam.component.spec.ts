import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ExamSubmitExamComponent } from './exam-submit-exam.component';

describe('ExamStartComponent', () => {
  let component: ExamSubmitExamComponent;
  let fixture: ComponentFixture<ExamSubmitExamComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ExamSubmitExamComponent]
    });
    fixture = TestBed.createComponent(ExamSubmitExamComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
