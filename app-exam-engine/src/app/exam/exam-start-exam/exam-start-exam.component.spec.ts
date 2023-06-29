import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ExamStartExamComponent } from './exam-start-exam.component';

describe('ExamStartComponent', () => {
  let component: ExamStartExamComponent;
  let fixture: ComponentFixture<ExamStartExamComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ExamStartExamComponent]
    });
    fixture = TestBed.createComponent(ExamStartExamComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
