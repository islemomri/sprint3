import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ListDevoirRenduComponent } from './list-devoir-rendu/list-devoir-rendu.component';
import { EvaluationDevoirComponent } from './evaluation-devoir/evaluation-devoir.component';

const routes: Routes = [
  { path : 'list/:id' , component:ListDevoirRenduComponent},
  { path: 'evaluer/:idDevoirRendu/:id' , component: EvaluationDevoirComponent }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class DevoirRenduRoutingModule { }
