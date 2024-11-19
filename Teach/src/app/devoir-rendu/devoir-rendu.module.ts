import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { DevoirRenduRoutingModule } from './devoir-rendu-routing.module';
import { ListDevoirRenduComponent } from './list-devoir-rendu/list-devoir-rendu.component';


@NgModule({
  declarations: [
    ListDevoirRenduComponent
  ],
  imports: [
    CommonModule,
    DevoirRenduRoutingModule
  ]
})
export class DevoirRenduModule { }
