import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormsModule } from '@angular/forms'; 
import { DevoirRenduRoutingModule } from './devoir-rendu-routing.module';
import { ListDevoirRenduComponent } from './list-devoir-rendu/list-devoir-rendu.component';
import { EvaluationDevoirComponent } from './evaluation-devoir/evaluation-devoir.component';


@NgModule({
  declarations: [
    ListDevoirRenduComponent,
    EvaluationDevoirComponent
  ],
  imports: [
    CommonModule,
    DevoirRenduRoutingModule,
    ReactiveFormsModule, 
    FormsModule 

  ]
})
export class DevoirRenduModule { }
