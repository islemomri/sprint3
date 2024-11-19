import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AddDevoirComponent } from './add-devoir.component';

describe('AddDevoirComponent', () => {
  let component: AddDevoirComponent;
  let fixture: ComponentFixture<AddDevoirComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [AddDevoirComponent]
    });
    fixture = TestBed.createComponent(AddDevoirComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
