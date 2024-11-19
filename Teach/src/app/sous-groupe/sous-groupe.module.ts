import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { SousGroupeRoutingModule } from './sous-groupe-routing.module';
import { AddSousGroupeComponent } from './add-sous-groupe/add-sous-groupe.component';
import { ButtonModule } from 'primeng/button';

import { AccordionModule } from 'primeng/accordion';
import { DividerModule } from 'primeng/divider';
import { PickListModule } from 'primeng/picklist';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { ListSousGroupesComponent } from './list-sous-groupes/list-sous-groupes.component';
import { AddEtudiantSousGroupeComponent } from './add-etudiant-sous-groupe/add-etudiant-sous-groupe.component';
import { DropdownModule } from 'primeng/dropdown';
import { ListboxModule } from 'primeng/listbox';

@NgModule({
  declarations: [
    AddSousGroupeComponent,
    ListSousGroupesComponent,
    AddEtudiantSousGroupeComponent
  ],
  imports: [
    CommonModule,
    SousGroupeRoutingModule,
    ButtonModule,
    PickListModule,
    FormsModule,
    ReactiveFormsModule,
    AccordionModule,
    DividerModule,
    DropdownModule,
    ListboxModule
  ]
})
export class SousGroupeModule { }