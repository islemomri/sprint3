import { Component, OnInit } from '@angular/core';
import { DevoirService } from '../service/devoir.service';
import { Router } from '@angular/router';
import { NgForm } from '@angular/forms';
import { IDevoir } from '../model/idevoir';
import { IDevoirDTO } from '../model/idevoir-dto';
import { SousGroupeService } from 'src/app/sous-groupe/service/sous-groupe.service';
import { ISousGroupe } from 'src/app/sous-groupe/model/isous-groupe';

@Component({
  selector: 'app-add-devoir',
  templateUrl: './add-devoir.component.html',
  styleUrls: ['./add-devoir.component.css']
})
export class AddDevoirComponent implements OnInit{
  sousgroupes!: ISousGroupe[];
  targetSousGroupes: any[] = [];
  pdfFile?: File;
  subbmited = false;
  nombreExercices = 0;
  totalDevoir = 0;
  showSousGroupes: boolean = false; 
  showSumOfQuestionsAlert: boolean = false;
  showSumOfExercisesAlert: boolean = false;
 

  exercices: {
    description: string;
    note: number;
    nombreQuestions: number;
    questions: { text: string; note: number }[];
  }[] = [];

  constructor(private service: DevoirService, private router: Router, private serviceSousGroupe: SousGroupeService) {}

  ngOnInit(): void {
    this.serviceSousGroupe.getAllSousgroupes(+localStorage.getItem("idCours")!).subscribe((sg) => {
      this.sousgroupes = sg;
    });
  }

  onSubmit(f: NgForm) {
    this.subbmited = true;

    if (f.invalid) {
      return;
    } else {
      this.addDevoir(f);
    }
  }

  addDevoir(f: NgForm) {
    const formData = new FormData();
    formData.append('typedevoir', f.value.typedevoir);
    formData.append('description', f.value.description);
    formData.append('ponderation', f.value.ponderation);
    formData.append('bareme', this.generateBaremeText());
    formData.append('dateLimite', f.value.dateLimite);
    formData.append('statut', f.value.statut);
    formData.append('typedevoir', f.value.typedevoir);
    formData.append('maxDocuments', f.value.maxDocuments);
    formData.append('sousGroupes', JSON.stringify(this.convertToListIntger()));

    if (this.pdfFile) {
      formData.append('pdf', this.pdfFile);
    }

    this.service.addDevoir(formData, +localStorage.getItem("idCours")!).subscribe((response) => {
      this.router.navigate(['/devoirs/list/' + +localStorage.getItem("idCours")!]);
    });
  }

  convertToListIntger() {
    return this.targetSousGroupes.map((sg) => sg.idSousGroupe);
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

  onFileChange(event: Event) {
    const input = event.target as HTMLInputElement;
    if (input.files && input.files.length) {
      this.pdfFile = input.files[0];
      if (this.pdfFile.size > 10 * 1024 * 1024) {
        alert('Le fichier est trop volumineux. Veuillez télécharger un fichier de moins de 10 Mo.');
        return;
      }
    }
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