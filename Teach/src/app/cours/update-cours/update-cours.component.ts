import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ICours } from '../model/icours';
import { ActivatedRoute, Router } from '@angular/router';
import { CoursService } from '../service/cours.service';
import { ICoursDTO } from '../model/icours-dto';

@Component({
  selector: 'app-update-cours',
  templateUrl: './update-cours.component.html',
  styleUrls: ['./update-cours.component.css']
})
export class UpdateCoursComponent {
  UpdateCoursForm!: FormGroup;
  cours!: ICours;
  submitted = false;
  role = localStorage.getItem("role");
  constructor(
    private formBuilder: FormBuilder,
    private activatedRoute: ActivatedRoute,
    private service: CoursService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.activatedRoute.params.subscribe((parametres) => {
      this.service.getCoursById(+parametres['id']).subscribe((c) => {
        if (c) {
          this.cours = c;
          console.log(c)
          this.initializeForm();
        }
      });
    });
  }

  initializeForm(): void {
    this.UpdateCoursForm = this.formBuilder.group({
      nom:[this.cours.nom,Validators.required],
      coefficient: [this.cours.coefficient, Validators.required],
      credits: [this.cours.credits, Validators.required],
    
    });
  }

  updateCours(): void {
    if (this.UpdateCoursForm.invalid) {
        return;
    }

    const values = this.UpdateCoursForm.value;
    const updatedCours: ICoursDTO = {
        nom: values.nom, // Assurez-vous que cela récupère la valeur saisie
        coefficient: values.coefficient, // Assurez-vous que cela récupère la valeur saisie
        credits: values.credits, // Ceci fonctionne déjà selon votre code
    };

    console.log('Updated Cours DTO:', updatedCours); // Vérifiez les valeurs dans la console

    this.service.updateCours(updatedCours, this.cours.idCours).subscribe(() => {
        this.router.navigate(['cours']);
    });
}


  onSubmit(): void {
    this.submitted = true;
    this.updateCours();
  }
}
