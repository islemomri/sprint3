import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ListCoursComponent } from './list-cours/list-cours.component';
import { AddCoursComponent } from './add-cours/add-cours.component';
import { UpdateCoursComponent } from './update-cours/update-cours.component';
import { InviteStudentComponent } from './invite-student/invite-student.component';
import { DeposerDocumentComponent } from './deposer-document/deposer-document.component';

const routes: Routes = [
  { path : '' , component:ListCoursComponent},
  { path : 'add' , component:AddCoursComponent},
  { path : 'update/:id' , component:UpdateCoursComponent},
  { path: 'invite-student', component: InviteStudentComponent },
  { path: 'deposer-document', component : DeposerDocumentComponent}
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class CoursRoutingModule { }
