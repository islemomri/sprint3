import { Component, OnInit } from '@angular/core';
import { SousGroupeService } from '../service/sous-groupe.service';
import { ActivatedRoute, Router } from '@angular/router';
import { CoursService } from 'src/app/cours/service/cours.service';
import { NgForm } from '@angular/forms';


@Component({
  selector: 'app-add-etudiant-sous-groupe',
  templateUrl: './add-etudiant-sous-groupe.component.html',
  styleUrls: ['./add-etudiant-sous-groupe.component.css']
})
export class AddEtudiantSousGroupeComponent implements OnInit{
  constructor(private service:SousGroupeService,private router:Router,private serviceCours:CoursService,private activatedRoute:ActivatedRoute){}
  allEtudiants!:any[];
  allEtudiantsSg!:any[];
  etudiants!:any[];
  idSg!:number;
  ngOnInit(): void {
    
    this.serviceCours.getCoursById(+localStorage.getItem("idCours")!).subscribe((c)=>{
      this.allEtudiants=c?.students!
      this.activatedRoute.params.subscribe(
        parametres=>{
          this.idSg=+parametres['id'];
       } )
       
       this.service.getSousgroupeById(this.idSg).subscribe((sg) => {
        this.allEtudiantsSg = sg?.etudiants || [];
        
        this.etudiants = this.allEtudiants.filter(etudiant =>
          !this.allEtudiantsSg.some(sgEtudiant => sgEtudiant.id === etudiant.id)
        );
        console.log(this.etudiants)
      });
     })
     
    
    
  }
  selectedEmail: string | undefined;
ajouterEtudiant(f: NgForm) {
  
console.log(this.selectedEmail)
    this.service.addEtudiantSousGroupe(this.idSg,this.selectedEmail!).subscribe(
      (response) => {
        console.log(response);
        this.router.navigate(["/sous-groupe"]);
      },
      (error) => {
        console.error('Erreur lors de l\'ajout de l\'étudiant', error);
      }
    );
  
}

}
