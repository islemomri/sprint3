import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { DevoirService } from '../service/devoir.service';
import { IDevoir } from '../model/idevoir';
import { IDevoirDTO } from '../model/idevoir-dto';
import { ISousGroupe } from 'src/app/sous-groupe/model/isous-groupe';
import { SousGroupeService } from 'src/app/sous-groupe/service/sous-groupe.service';

@Component({
    selector: 'app-update-devoir',
    templateUrl: './update-devoir.component.html',
    styleUrls: ['./update-devoir.component.css']
})
export class UpdateDevoirComponent implements OnInit {
    UpdateDevoirForm!: FormGroup;
    devoir!: IDevoir;
    showSousGroupes: boolean = false; 
    nombreExercices = 0;
    totalDevoir = 0;
    showSumOfQuestionsAlert: boolean = false;
  showSumOfExercisesAlert: boolean = false;
  showmodifbareme: boolean = false; 
  exercices: {
    description: string;
    note: number;
    nombreQuestions: number;
    questions: { text: string; note: number }[];
  }[] = [];
    constructor(
        private formBuilder: FormBuilder,
        private activatedRoute: ActivatedRoute,
        private service: DevoirService,
        private router: Router,
        private serviceSousGroupe:SousGroupeService,
        private cdr: ChangeDetectorRef
    ) {}

    ngOnInit(): void {
      this.activatedRoute.params.subscribe((params) => {
          this.service.getDevoirById(+params['id']).subscribe((d) => {
              if (d) {
                  this.devoir = d;
                  console.log(d);
                  this.initializeForm(); // Initialisez le formulaire
                this.cdr.detectChanges();
                  this.targetSousGroupes=d.sousGroupes
                  this.serviceSousGroupe.getAllSousgroupes(+localStorage.getItem("idCours")!).subscribe((sg)=>{
                    this.sousgroupes=sg
                    this.sousgroupes = this.sousgroupes.filter(sg => !this.targetSousGroupes.some(target => target.idSousGroupe === sg.idSousGroupe));
               
                  })
              }
          });
      });
      
       
      
  }
  
  initializeForm(): void {
    console.log('Devoir:', this.devoir);  // Vérifiez les valeurs de `devoir`
    this.UpdateDevoirForm = this.formBuilder.group({
        description: [this.devoir.description, Validators.required],
        typedevoir: [this.devoir.typedevoir, Validators.required],
        ponderation: [this.devoir.ponderation, Validators.required],
        bareme: [this.devoir.bareme, Validators.required],
        statut: [this.devoir.statut, Validators.required],
        dateLimite: [this.devoir.dateLimite, Validators.required],
        maxDocuments: [this.devoir.maxDocuments || 1, [Validators.required]],
    });
}

  

    updateDevoir(): void {
        if (this.UpdateDevoirForm.invalid) {
            return;
        }

        const values = this.UpdateDevoirForm.value;
        console.log('Form Values:', values); 
        const updatedDevoir: IDevoirDTO = {
            description: values.description,
            typedevoir: values.typedevoir,
            ponderation: values.ponderation,
            bareme: values.bareme,
            statut:values.statut,
            maxDocuments: values.maxDocuments,
            dateLimite: new Date(values.dateLimite),
            sousGroupes: JSON.stringify(this.convertToListIntger())
        };
        this.service.updateDevoir(updatedDevoir, this.devoir.idDevoir).subscribe(() => {
            this.router.navigate(['devoirs']);
        });
    }
    targetSousGroupes:any[]=[];
    sousgroupes!:ISousGroupe[];
    convertToListIntger(){
        let list:Array<number>=[];
        this.targetSousGroupes.forEach(element => {
          for (let index = 0; index < this.targetSousGroupes.length; index++) {
            const element = this.targetSousGroupes[index];
            list.push(element.idSousGroupe);
          }
        });
        return list;
      }


      
    
      updateTotalNotes() {
        this.sumOfExerciceNotes(); 
      }
    
      generateBaremeText(): string {
        return this.exercices
          .map((exercice, index) => {
            const questionsText = exercice.questions
              .map((question, qIndex) => `Question ${qIndex + 1} - Note: ${question.note}`)
              .join('\n');
            return `Exercice ${index + 1} - Note: ${exercice.note}\n${questionsText}`;
          })
          .join('\n');
      }
    
    
   
    
      onNombreExercicesChange() {
        this.exercices = new Array(this.nombreExercices).fill(null).map(() => ({
            description: '',
            note: 0,
            nombreQuestions: 0,
            questions: []
        }));
        this.updateTotalNotes();
    }
    
      onNombreQuestionsChange(i: number) {
        const nombreQuestions = this.exercices[i].nombreQuestions;
        this.exercices[i].questions = new Array(nombreQuestions).fill(null).map(() => ({
          text: '',
          note: 0
        }));
        this.updateTotalNotes();
      }
    
    
    
      sumOfQuestions(i: number): number {
        let sum = 0;
        if (this.exercices[i].questions) {
            sum = this.exercices[i].questions.reduce((acc, question) => acc + (question.note || 0), 0);
        }
        return sum;
    }
    sumOfExerciceNotes(): number {
      return this.exercices.reduce((acc, exercice) => acc + (exercice.note || 0), 0);
    }
    
   
    
    isFormValid(): boolean {
      return this.sumOfExerciceNotes() === this.totalDevoir;
    }
    // Méthode pour vérifier si les notes des exercices sont valides
    hasInvalidExerciseNotes(): boolean {
      return this.exercices.some((exercice, index) => this.sumOfQuestions(index) !== exercice.note);
    }
}