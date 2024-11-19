import { Component } from '@angular/core';
import { CoursService } from '../service/cours.service';
import { Router } from '@angular/router';
import { NgForm } from '@angular/forms';
import { ICours } from '../model/icours';

@Component({
  selector: 'app-add-cours',
  templateUrl: './add-cours.component.html',
  styleUrls: ['./add-cours.component.css']
})
export class AddCoursComponent {
constructor(private service:CoursService,private router:Router){}
subbmited=false;

onSubmit(f:NgForm){
  this.subbmited=true;
  
    if(f.invalid){
      return
    }
    else{
this.addCours(f)
}
}

addCours=(f:NgForm)=>{
 const newCours={nom:f.value.nom,coefficient:f.value.coefficient,credits:f.value.credits}
 this.service.addCours(newCours as ICours).subscribe(
  response => {
 
    // console.log('Cours added successfully:', response);
    this.router.navigate(["/cours"]);
  },
 
 );
 
}
}
