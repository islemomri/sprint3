import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ListDevoirComponent } from './list-devoir.component';

describe('ListDevoirComponent', () => {
  let component: ListDevoirComponent;
  let fixture: ComponentFixture<ListDevoirComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ListDevoirComponent]
    });
    fixture = TestBed.createComponent(ListDevoirComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
