import { Component, OnInit } from '@angular/core';
import { SousGroupeService } from '../service/sous-groupe.service';
import { Router } from '@angular/router';
import { CoursService } from 'src/app/cours/service/cours.service';
import { NgForm } from '@angular/forms';

@Component({
  selector: 'app-list-sous-groupes',
  templateUrl: './list-sous-groupes.component.html',
  styleUrls: ['./list-sous-groupes.component.css']
})
export class ListSousGroupesComponent implements OnInit {
  sgList!: any[];
  allEtudiants!: any[];
  allEtudiantsSg!: any[];
  etudiants!: any[];
  isPopupVisible = false;
  selectedEtudiant: any;
  idSg!: number;

  constructor(private service: SousGroupeService, private router: Router, private serviceCours: CoursService) {}

  ngOnInit(): void {
    this.serviceCours.getCoursById(+localStorage.getItem("idCours")!).subscribe((c) => {
      this.allEtudiants = c?.students!;
    });
    this.service.getAllSousgroupes(+localStorage.getItem("idCours")!).subscribe((sg) => {
      this.sgList = sg;
    }, (error) => {
      console.error('Error fetching cours', error);
    });
  }

  closePopup() {
    this.isPopupVisible = false;
  }

  ajouterEtudiant(f: NgForm) {
    if (this.selectedEtudiant) {
      this.service.addEtudiantSousGroupe(this.idSg, this.selectedEtudiant).subscribe(
        (response) => {
          console.log('Étudiant ajouté avec succès');
          this.closePopup();
        },
        (error) => {
          console.error('Erreur lors de l\'ajout de l\'étudiant', error);
        }
      );
    }
  }

  onEtudiantSelect(etudiant: any) {
    this.selectedEtudiant = etudiant;
  }

  showPopup(idSousGroupe: number): void {
    this.isPopupVisible = true;
  
    // Fetch sous-groupe students
    this.service.getSousgroupeById(idSousGroupe).subscribe(sousGroupe => {
      this.allEtudiantsSg = sousGroupe!.etudiants || []; // Ensure it's always an array
  
      // Assuming allEtudiants is defined earlier in your component
      if (this.allEtudiants) {
        // Safely filter the students who are not in the current sous-groupe
        this.allEtudiants = this.allEtudiants.filter(student =>
          !this.allEtudiantsSg.some(sgStudent => sgStudent.email === student.email)
        );
      }
    });
  }
  
}
