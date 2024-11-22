import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { DevoirRenduService } from '../service/devoir-rendu.service';
import { EvaluationDTO} from '../model/evaluation-dto';
import { IDevoirRendu } from '../model/idevoir-rendu';
import { DevoirService } from 'src/app/devoir/service/devoir.service';
import { IDevoir } from 'src/app/devoir/model/idevoir';

@Component({
  selector: 'app-evaluation-devoir',
  templateUrl: './evaluation-devoir.component.html',
  styleUrls: ['./evaluation-devoir.component.css']
})
export class EvaluationDevoirComponent implements OnInit {
  evaluation: EvaluationDTO = {
    idDevoirRendu: 0,
    note: 0,
    commentaire: '',
  
  };
  idDevoir!: number;  // Variable pour stocker l'ID du devoir récupéré de l'URL
  devoirRendu!: IDevoirRendu;  // Variable pour stocker les détails du devoir rendu
  devoir!: IDevoir;  // Variable pour stocker les détails du devoir
  commentaire: string = '';  
  
  constructor(
    private route: ActivatedRoute,
    private devoirRenduService: DevoirRenduService,
    private devoirserv : DevoirService
  ) {}

  ngOnInit(): void {
    this.route.paramMap.subscribe(params => {
      const idDevoirRenduString =params.get('idDevoirRendu');
      const idDevoirString = params.get('id');
      console.log('Paramètres récupérés - idDevoirRendu:', idDevoirRenduString, 'idDevoir:', idDevoirString);

  
      if (idDevoirRenduString && idDevoirString) {
        const idDevoirRendu = +idDevoirRenduString;
        this.idDevoir = +idDevoirString;
        console.log('APres convertion number - idDevoirRendu:', idDevoirRendu, 'idDevoir:', this.idDevoir);
        // Appel pour récupérer les détails du devoir
        this.devoirserv.getDevoirById(this.idDevoir).subscribe((devoir: IDevoir | null) => {
          if (devoir) {
            this.devoir = devoir;  // Stocker les détails du devoir dans la variable `devoir`
            this.commentaire = devoir.bareme || '';
            console.log('Détails du devoirrrrrrrrrr récupérés:', devoir);
          } else {
            console.error('Devoir non trouvé');
          }
        });
console.log('hhhhhhhhhhhhhhhh',idDevoirRendu);
        // Appel pour récupérer les détails du devoir rendu
        this.devoirRenduService.getDevoirRenduById(idDevoirRendu).subscribe((devoirRendu: IDevoirRendu | null) => {
          if (devoirRendu) {
            this.devoirRendu = devoirRendu;  // Stocker les détails du devoir rendu dans la variable `devoirRendu`
            this.evaluation.idDevoirRendu = idDevoirRendu;
            this.evaluation.note = devoirRendu.note ?? 0;
            this.evaluation.commentaire = devoirRendu.commentaire ?? '';
          } else {
            console.error('Devoir rendu non trouvé');
          }
        });
      } else {
        console.error('ID, ID Devoir Rendu ou ID Devoir manquants');
      }
    });
  }
  

  onSubmitEvaluation(form: any) {
    if (form.valid) {
      console.log("Données envoyées pour évaluation:", this.evaluation); // Journalisation

      this.devoirRenduService.evaluerDevoir(this.evaluation).subscribe(
        response => {
          alert('Évaluation soumise avec succès');
          console.log("Réponse du serveur:", response); // Journalisation
        },
        error => {
          console.error('Erreur lors de la soumission de l\'évaluation', error);
        }
      );
    }
  }
}
