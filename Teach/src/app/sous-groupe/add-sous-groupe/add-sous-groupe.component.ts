import { Component, OnInit } from '@angular/core';
import { SousGroupeService } from '../service/sous-groupe.service';
import { NgForm } from '@angular/forms';
import { Router } from '@angular/router';
import { ISousGroupe } from '../model/isous-groupe';
import { CoursService } from 'src/app/cours/service/cours.service';

@Component({
  selector: 'app-add-sous-groupe',
  templateUrl: './add-sous-groupe.component.html',
  styleUrls: ['./add-sous-groupe.component.css']
})
export class AddSousGroupeComponent implements OnInit{
  constructor(private service:SousGroupeService,private router:Router,private serviceCours:CoursService){}
  ngOnInit(): void {
   this.serviceCours.getCoursById(+localStorage.getItem("idCours")!).subscribe((c)=>{
    this.etudiants=c?.students!;
    })
  }
 
  etudiants:Array<{
    id: number;
    nom: string;
    prenom: string;
    email: string;
    motDePasse: string;
}>=[]
  targetEtudiants:any[]=[];
convertToListString(){
  let list:Array<string>=[];
  this.targetEtudiants.forEach(element => {
    for (let index = 0; index < this.targetEtudiants.length; index++) {
      const element = this.targetEtudiants[index];
      list.push(element.email);
    }
  });
  return list;
}
  addSousGroupe=(f:NgForm)=>{
    const newSousGroupe={nom:f.value.nom,etudiants:this.convertToListString()}
    console.log(this.targetEtudiants)
    this.service.addSousgroupe(newSousGroupe as ISousGroupe,+localStorage.getItem("idCours")!).subscribe(
     response => {
    
       // console.log('Cours added successfully:', response);
       this.router.navigate(["/cours"]);
     },
    
    );
    
   }
}
