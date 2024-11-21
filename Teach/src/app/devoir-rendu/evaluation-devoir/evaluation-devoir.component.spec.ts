import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EvaluationDevoirComponent } from './evaluation-devoir.component';

describe('EvaluationDevoirComponent', () => {
  let component: EvaluationDevoirComponent;
  let fixture: ComponentFixture<EvaluationDevoirComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [EvaluationDevoirComponent]
    });
    fixture = TestBed.createComponent(EvaluationDevoirComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
