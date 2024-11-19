import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AddSousGroupeComponent } from './add-sous-groupe/add-sous-groupe.component';
import { ListSousGroupesComponent } from './list-sous-groupes/list-sous-groupes.component';
import { AddEtudiantSousGroupeComponent } from './add-etudiant-sous-groupe/add-etudiant-sous-groupe.component';

const routes: Routes = [
  { path : 'add' , component:AddSousGroupeComponent},
  
  { path : '' , component:ListSousGroupesComponent},
  {
    path:'addEtudiant/:id',component:AddEtudiantSousGroupeComponent
  }
  
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class SousGroupeRoutingModule { }