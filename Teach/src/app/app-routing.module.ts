import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

const routes: Routes = [
  {path:'auth',loadChildren:()=>import('./auth/auth.module' ).then(m=>m.AuthModule)},
  {path:'cours',loadChildren:()=>import('./cours/cours.module' ).then(m=>m.CoursModule)},
  {path:'devoirs',loadChildren:()=>import('./devoir/devoir.module' ).then(m=>m.DevoirModule)},
  {path:'sous-groupe',loadChildren:()=>import('./sous-groupe/sous-groupe.module' ).then(m=>m.SousGroupeModule)},
  {path:'devoirRendu',loadChildren:()=>import('./devoir-rendu/devoir-rendu.module' ).then(m=>m.DevoirRenduModule)},
  
  
  {
    path:'',redirectTo:'auth/login',pathMatch:'full',
  }

];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
