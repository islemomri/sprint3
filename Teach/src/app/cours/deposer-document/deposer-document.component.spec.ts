import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DeposerDocumentComponent } from './deposer-document.component';

describe('DeposerDocumentComponent', () => {
  let component: DeposerDocumentComponent;
  let fixture: ComponentFixture<DeposerDocumentComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [DeposerDocumentComponent]
    });
    fixture = TestBed.createComponent(DeposerDocumentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
