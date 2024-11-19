import { ComponentFixture, TestBed } from '@angular/core/testing';
import { AddEtudiantSousGroupeComponent } from './add-etudiant-sous-groupe.component';
import { SousGroupeService } from '../service/sous-groupe.service';
import { CoursService } from 'src/app/cours/service/cours.service';
import { ActivatedRoute, Router } from '@angular/router';
import { of } from 'rxjs';
import { FormsModule, NgForm } from '@angular/forms';
import { RouterTestingModule } from '@angular/router/testing';
import { ICours } from 'src/app/cours/model/icours';

describe('AddEtudiantSousGroupeComponent', () => {
  let component: AddEtudiantSousGroupeComponent;
  let fixture: ComponentFixture<AddEtudiantSousGroupeComponent>;
  let sousGroupeService: SousGroupeService;
  let coursService: CoursService;
  let activatedRoute: ActivatedRoute;
  let router: Router;

  beforeEach(() => {
    const coursServiceSpy = jasmine.createSpyObj('CoursService', ['getCoursById']);
    const sousGroupeServiceSpy = jasmine.createSpyObj('SousGroupeService', ['getSousgroupeById', 'addEtudiantSousGroupe']);
  
    TestBed.configureTestingModule({
      imports: [RouterTestingModule, FormsModule],
      declarations: [AddEtudiantSousGroupeComponent],
      providers: [
        { provide: SousGroupeService, useValue: sousGroupeServiceSpy },
        { provide: CoursService, useValue: coursServiceSpy },
        { provide: ActivatedRoute, useValue: { params: of({ id: 1 }) } }
      ]
    });
  
    fixture = TestBed.createComponent(AddEtudiantSousGroupeComponent);
    component = fixture.componentInstance;
    sousGroupeService = TestBed.inject(SousGroupeService);
    coursService = TestBed.inject(CoursService);
    activatedRoute = TestBed.inject(ActivatedRoute);
    router = TestBed.inject(Router);
  });
  
  
  // Add a simple test case to verify the setup
  it('should have a valid test environment', () => {
    expect(true).toBeTrue();
  });

  it('should create the component', () => {
    expect(component).toBeTruthy();
   });

  // it('should fetch all students and filter those not in the sous-groupe', () => {
  //   const coursServiceSpy = TestBed.inject(CoursService) as jasmine.SpyObj<CoursService>;
  //   const sousGroupeServiceSpy = TestBed.inject(SousGroupeService) as jasmine.SpyObj<SousGroupeService>;
  
  //   coursServiceSpy.getCoursById.and.returnValue(of({
  //     idCours: 101,
  //     nom: "Programmation Web Avancée",
  //     coefficient: 2,
  //     credits: 5,
  //     code: "WEB202",
  //     enseignant: {
  //       id: 1,
  //       nom: "Dupont",
  //       prenom: "Jean",
  //       email: "jean.dupont@example.com",
  //       motDePasse: "password123",
  //       specialite: "Développement Web",
  //     },
  //     students: [
  //       {
  //         id: 201,
  //         nom: "Martin",
  //         prenom: "Alice",
  //         email: "alice.martin@example.com",
  //         motDePasse: "alicepass",
  //       },
  //       {
  //         id: 202,
  //         nom: "Lefevre",
  //         prenom: "Paul",
  //         email: "paul.lefevre@example.com",
  //         motDePasse: "paulpass",
  //       },
  //     ],
  //     methodeCalcul: "Moyenne",
  //   }));
  
  //   sousGroupeServiceSpy.getSousgroupeById.and.returnValue(of({
  //     idSousGroupe: 1,
  //     nom: 'Group A',
  //     etudiants: [
  //       'alice.martin@example.com'
  //     ]
  //   }));
  
  //   component.ngOnInit();
  
  //   fixture.detectChanges();
  
  //   const filteredStudents = [
  //     { id: 202, nom: 'Lefevre', prenom: 'Paul', email: 'paul.lefevre@example.com', motDePasse: 'paulpass' }
  //   ];
  
  //   expect(component.etudiants).toEqual(filteredStudents);
  // });
  
  
});
