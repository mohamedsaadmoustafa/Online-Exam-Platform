import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ExamInstanceComponent } from './exam-instance.component';

describe('ExamStartComponent', () => {
  let component: ExamInstanceComponent;
  let fixture: ComponentFixture<ExamInstanceComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ExamInstanceComponent]
    });
    fixture = TestBed.createComponent(ExamInstanceComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
