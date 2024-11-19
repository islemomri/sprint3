import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ListSousGroupesComponent } from './list-sous-groupes.component';
import { SousGroupeService } from '../service/sous-groupe.service';
import { CoursService } from 'src/app/cours/service/cours.service';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { Router } from '@angular/router';
import { of } from 'rxjs';
import { ISousGroupe } from '../model/isous-groupe'; // Adjust the import path as needed
import { DividerModule } from 'primeng/divider'; // Import the PrimeNG Divider module
import { CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { NgForm } from '@angular/forms';
import { ICours } from 'src/app/cours/model/icours';

describe('ListSousGroupesComponent', () => {
  let component: ListSousGroupesComponent;
  let fixture: ComponentFixture<ListSousGroupesComponent>;
  let sousGroupeService: SousGroupeService;
  let coursService: CoursService;
  let router: Router;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        HttpClientTestingModule,
        DividerModule // Add the PrimeNG Divider module here
      ],
      declarations: [ListSousGroupesComponent],
      providers: [
        SousGroupeService,
        CoursService,
        Router
      ],
      schemas: [CUSTOM_ELEMENTS_SCHEMA] // Add this line to handle custom elements
    });

    fixture = TestBed.createComponent(ListSousGroupesComponent);
    component = fixture.componentInstance;
    sousGroupeService = TestBed.inject(SousGroupeService);
    coursService = TestBed.inject(CoursService);
    router = TestBed.inject(Router);

    // Spy on service methods
    const mockCours: ICours = {
      idCours: 1,
      nom: 'Cours 1',
      coefficient: 1,
      credits: 3,
      code: 'C1',
      enseignant: {
        id: 1,
        nom: 'Enseignant',
        prenom: 'Prenom',
        email: 'enseignant@example.com',
        motDePasse: 'password',
        specialite: 'Specialite'
      },
      students: [
        { id: 1, nom: 'Student', prenom: 'One', email: 'student1@example.com', motDePasse: 'password' }
      ],
      methodeCalcul: 'Method'
    };
    const mockSousGroupes: ISousGroupe[] = [
      {
        idSousGroupe: 1,
        nom: 'Group 1',
        etudiants: [
         'student1@example.com' 
        ]
      }
    ];
    spyOn(coursService, 'getCoursById').and.returnValue(of(mockCours));
    spyOn(sousGroupeService, 'getAllSousgroupes').and.returnValue(of(mockSousGroupes));
    spyOn(sousGroupeService, 'getSousgroupeById').and.returnValue(of(mockSousGroupes[0]));
    spyOn(sousGroupeService, 'addEtudiantSousGroupe').and.returnValue(of({}));
  });

  it('should create the component', () => {
    expect(component).toBeTruthy();
  });

  it('should fetch all students and sous-groupes on init', () => {
    component.ngOnInit();

    expect(coursService.getCoursById).toHaveBeenCalledWith(+localStorage.getItem("idCours")!);
    expect(sousGroupeService.getAllSousgroupes).toHaveBeenCalledWith(+localStorage.getItem("idCours")!);
    expect(component.allEtudiants).toEqual([
      { id: 1, nom: 'Student', prenom: 'One', email: 'student1@example.com', motDePasse: 'password' }
    ]);
    expect(component.sgList).toEqual([
      {
        idSousGroupe: 1,
        nom: 'Group 1',
        etudiants: [
          'student1@example.com' 
        ]
      }
    ]);
  });

  it('should show popup and fetch sous-groupe students', () => {
    component.showPopup(1);

    expect(component.isPopupVisible).toBeTrue();
    expect(sousGroupeService.getSousgroupeById).toHaveBeenCalledWith(1);
    expect(component.allEtudiantsSg).toEqual([
      { id: 1, name: 'Student 1', email: 'student1@example.com' }
    ]);
  });

  it('should add student to sous-groupe', () => {
    component.selectedEtudiant = { id: 2, name: 'Student 2', email: 'student2@example.com' };
    component.idSg = 1;

    component.ajouterEtudiant({} as NgForm);

    expect(sousGroupeService.addEtudiantSousGroupe).toHaveBeenCalledWith(1,  'student2@example.com' );
  });
});
