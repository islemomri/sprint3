import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ListDevoirComponent } from './list-devoir/list-devoir.component';
import { AddDevoirComponent } from './add-devoir/add-devoir.component';
import { UpdateCoursComponent } from '../cours/update-cours/update-cours.component';
import { UpdateDevoirComponent } from './update-devoir/update-devoir.component';

const routes: Routes = [
  { path : 'list/:id' , component:ListDevoirComponent},
  { path : 'add' , component:AddDevoirComponent},
  { path : 'update/:id' , component:UpdateDevoirComponent},
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class DevoirRoutingModule { }
