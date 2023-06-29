import { ComponentFixture, TestBed } from '@angular/core/testing';
import {ExamDefinitionComponent} from "./exam-definition.component";



describe('ExamStartComponent', () => {
  let component: ExamDefinitionComponent;
  let fixture: ComponentFixture<ExamDefinitionComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ExamDefinitionComponent]
    });
    fixture = TestBed.createComponent(ExamDefinitionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
