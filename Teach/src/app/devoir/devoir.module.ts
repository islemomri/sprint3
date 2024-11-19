import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MessageService } from 'primeng/api';
import { DevoirRoutingModule } from './devoir-routing.module';
import { AddDevoirComponent } from './add-devoir/add-devoir.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { ListDevoirComponent } from './list-devoir/list-devoir.component';
import { UpdateDevoirComponent } from './update-devoir/update-devoir.component';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { ToastModule } from 'primeng/toast';
import { ButtonModule } from 'primeng/button';
import { PickListModule } from 'primeng/picklist';

@NgModule({
  declarations: [
    AddDevoirComponent,
    ListDevoirComponent,
    UpdateDevoirComponent
  ],
  imports: [
    CommonModule,
    DevoirRoutingModule,
    ReactiveFormsModule,
    ConfirmDialogModule,
    ToastModule,
    ButtonModule,
    FormsModule,
    
    PickListModule,
    
  ]
})
export class DevoirModule { }
