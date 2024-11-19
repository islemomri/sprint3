import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ListDevoirRenduComponent } from './list-devoir-rendu.component';
import { DevoirRenduService } from '../service/devoir-rendu.service';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute, Router } from '@angular/router';
import { of } from 'rxjs';

describe('ListDevoirRenduComponent', () => {
  let component: ListDevoirRenduComponent;
  let fixture: ComponentFixture<ListDevoirRenduComponent>;
  let service: DevoirRenduService;
  let activatedRoute: ActivatedRoute;
  let router: Router;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [ListDevoirRenduComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { params: of({ id: '1' }) }
        },
        Router
      ]
    });

    fixture = TestBed.createComponent(ListDevoirRenduComponent);
    component = fixture.componentInstance;
    service = TestBed.inject(DevoirRenduService);
    activatedRoute = TestBed.inject(ActivatedRoute);
    router = TestBed.inject(Router);

    // Spy on service methods
    spyOn(service, 'getAllDevoirsRendu').and.callThrough();
    spyOn(service, 'downloadDevoirRenduPDF').and.returnValue(of(new Blob()));
  });

  it('should create the component', () => {
    expect(component).toBeTruthy();
  });

  it('should fetch the devoirs rendu on init', () => {
    const mockDevoirs = [{ idDevoirRendu: 1, nomDevoir: 'Devoir 1' }];
    (service.getAllDevoirsRendu as jasmine.Spy).and.returnValue(of(mockDevoirs));

    component.ngOnInit();

    // Verify that the method `getAllDevoirsRendu` is called with the expected ID
    expect(service.getAllDevoirsRendu).toHaveBeenCalledWith(1);
    
    // Verify that the list of devoirs is correctly assigned
    expect(component.listDevoirRendu).toEqual(mockDevoirs);
  });

  it('should download devoir rendu PDF when downloadRenduPDF is called', () => {
    const email = 'test@example.com';
    const a = document.createElement('a');
    const clickSpy = spyOn(a, 'click'); // Spy on the click method
    spyOn(document, 'createElement').and.returnValue(a); // Ensure the created element is the spied one
  
    component.downloadRenduPDF(email);
  
    expect(clickSpy).toHaveBeenCalled(); // Verify that click() was called
  });
});
