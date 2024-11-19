import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UpdateDevoirComponent } from './update-devoir.component';

describe('UpdateDevoirComponent', () => {
  let component: UpdateDevoirComponent;
  let fixture: ComponentFixture<UpdateDevoirComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [UpdateDevoirComponent]
    });
    fixture = TestBed.createComponent(UpdateDevoirComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
