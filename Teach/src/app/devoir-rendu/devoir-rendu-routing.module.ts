import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ListDevoirRenduComponent } from './list-devoir-rendu/list-devoir-rendu.component';

const routes: Routes = [
  { path : 'list/:id' , component:ListDevoirRenduComponent},
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class DevoirRenduRoutingModule { }
