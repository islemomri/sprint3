import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { SousGroupeService } from './sous-groupe.service';
import { AuthService } from 'src/app/auth/service/auth.service';
import { HttpHeaders } from '@angular/common/http';

describe('SousGroupeService', () => {
  let service: SousGroupeService;
  let httpMock: HttpTestingController;
  let authServiceSpy: jasmine.SpyObj<AuthService>;

  beforeEach(() => {
    const spy = jasmine.createSpyObj('AuthService', ['createAuthorizationHeader', 'getToken']);
    
    // Mock localStorage with valid values for the tests
    spyOn(localStorage, 'getItem').and.returnValue('1');  // Mock a valid idCours (1)

    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [
        SousGroupeService,
        { provide: AuthService, useValue: spy }
      ]
    });

    service = TestBed.inject(SousGroupeService);
    httpMock = TestBed.inject(HttpTestingController);
    authServiceSpy = TestBed.inject(AuthService) as jasmine.SpyObj<AuthService>;

    // Mock behavior of AuthService methods
    authServiceSpy.getToken.and.returnValue('test-token');  // Ensure getToken returns the mock token
    authServiceSpy.createAuthorizationHeader.and.callFake(() => {
      return new HttpHeaders().set('Authorization', 'Bearer test-token');
    });
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should fetch all sous-groupes', () => {
    const dummySousGroupes = [
      { idSousGroupe: 1, nom: 'Groupe 1', etudiants: [] },
      { idSousGroupe: 2, nom: 'Groupe 2', etudiants: [] }
    ];

    service.getAllSousgroupes(1).subscribe(sousGroupes => {
      expect(sousGroupes.length).toBe(2);
      expect(sousGroupes).toEqual(dummySousGroupes);
    });

    const req = httpMock.expectOne('http://localhost:9090/sousgroupe/1');
    expect(req.request.method).toBe('GET');
    expect(req.request.headers.get('Authorization')).toBe('Bearer test-token');
    req.flush(dummySousGroupes);
  });

  it('should add a new sous-groupe', () => {
    const dummySousGroupe = { idSousGroupe: 1, nom: 'New Group', etudiants: [] };
    const courseId = 123;

    service.addSousgroupe(dummySousGroupe, courseId).subscribe(response => {
      expect(response).toEqual(dummySousGroupe);
    });

    const req = httpMock.expectOne(`http://localhost:9090/addsousgroupe/${courseId}`);
    expect(req.request.method).toBe('POST');
    expect(req.request.headers.get('Authorization')).toBe('Bearer test-token');
    req.flush(dummySousGroupe);
  });

  it('should add an etudiant to sous-groupe', () => {
    const idSousGroupe = 1;
    const email = 'etudiant@example.com';
    const mockResponse = { message: 'Etudiant added successfully' };

    service.addEtudiantSousGroupe(idSousGroupe, email).subscribe(response => {
      expect(response).toEqual(mockResponse);
    });

    const req = httpMock.expectOne(`http://localhost:9090/addEtudiantSousgroupe/${idSousGroupe}/${email}`);
    expect(req.request.method).toBe('POST');

    // Log the header to check if the Authorization header is set correctly
    console.log('Request headers:', req.request.headers);

    expect(req.request.headers.get('Authorization')).toBe('Bearer test-token');
    req.flush(mockResponse);
  });

  afterAll(() => {
    httpMock.verify();
  });

  it('should get sous-groupe by id', () => {
    const dummySousGroupes = [
      { idSousGroupe: 1, nom: 'Groupe 1', etudiants: [] },
      { idSousGroupe: 2, nom: 'Groupe 2', etudiants: [] }
    ];
    
    service.getSousgroupeById(1).subscribe(sousGroupe => {
      expect(sousGroupe).toEqual(dummySousGroupes[0]);
    });

    const req = httpMock.expectOne('http://localhost:9090/sousgroupe/1');
    expect(req.request.method).toBe('GET');
    req.flush(dummySousGroupes);
  });

  afterAll(() => {
    // Ensure that no HTTP requests are still pending
    httpMock.verify();
  });
});
