import { Component, DoCheck, OnInit } from '@angular/core';
import { IDevoir } from '../model/idevoir';
import { DevoirService } from '../service/devoir.service';
import { ActivatedRoute, Router } from '@angular/router';
import { ConfirmationService, MessageService } from 'primeng/api';
import { DevoirRenduService } from 'src/app/devoir-rendu/service/devoir-rendu.service';
import { CoursService } from 'src/app/cours/service/cours.service';
import { NgForm } from '@angular/forms';
import { DomSanitizer, SafeResourceUrl } from '@angular/platform-browser';

@Component({
  selector: 'app-list-devoir',
  templateUrl: './list-devoir.component.html',
  styleUrls: ['./list-devoir.component.css'],
  providers: [ConfirmationService, MessageService]
})
export class ListDevoirComponent implements OnInit, DoCheck {
  devoirs: IDevoir[] = [];
  sanitizedUrls: SafeResourceUrl[] = [];
  documentsList: any[] = [];
  courId!: number;
  courseCode: string = '';
  courseNom: string ='';
  role: string | null = '';
  isPopupVisible = false;
  rendu = false;
  test!: boolean;
  maxFiles!:number;
  pdfFile?: File;
  pdfFiles: File[] = [];
  subbmited = false;
  studentEmail: string = '';
  teacherEmail: string = '';
  devoirNoteStatus: { [key: number]: boolean } = {};
  // Variables pour gérer les messages de succès et d'erreur
invitationStudentSuccess: string = '';
invitationStudentError: string = '';
invitationTeacherSuccess: string = '';
invitationTeacherError: string = '';
invitationStudentMessageClass: string = ''; // Classe CSS pour style de message
invitationTeacherMessageClass: string = ''; // Classe CSS pour style de message
selectedDevoir: IDevoir | null = null;  // Variable pour stocker le devoir sélectionné
evaluationNote: number | null = null;  // To store the grade
evaluationcommentaire: string = '';  // To store the feedback
  // Autres variables et méthodes...
  isPopupVisibleeval = false;
  constructor(
    private devoirService: DevoirService,
    private devoirRService: DevoirRenduService,
    private activatedRoute: ActivatedRoute,
    private router: Router,
    private confirmationService: ConfirmationService,
    private messageService: MessageService,
    private coursService: CoursService,
    private sanitizer: DomSanitizer
    
  ) {}

  ngOnInit(): void {
    this.role = localStorage.getItem('role');
    this.activatedRoute.params.subscribe((params) => {
      this.courId = +params['id'];
      localStorage.setItem('idCours', params['id']);
      console.log('Username:', localStorage.getItem("username"));
      console.log('Course ID:', this.courId);

      if (this.role === 'etudiant') {
        this.devoirService.getDevoirsByEtudiantId(localStorage.getItem("username")!, this.courId)
          .subscribe((d) => {
            this.devoirs = d || [];
            this.devoirs.forEach((devoir) => {
              this.getDevoirRendu(devoir.idDevoir, localStorage.getItem("username")!);  // Retrieve the report for each devoir
            });
            console.log("Les devoirs récupérés :", this.devoirs);
          });
      }
      else {
        this.devoirService.getAllDevoirs(this.courId)
          .subscribe((d) => {
            this.devoirs = d.map(devoir => ({
              ...devoir,
            })) || [];
            console.log("les devoirs:", this.devoirs);
          });
        this.getCourseCode();
      }
    });
  }


  
  showEvaluationPopup(devoir: IDevoir): void {
    this.selectedDevoir = devoir;
    
    // Fetch the devoir rendu (submitted assignment) for the specific student and devoir
    this.devoirRService.getDevoirRendubyetudiantdevoir(devoir.idDevoir, localStorage.getItem("username")!)
      .subscribe(
        (devoirRendu) => {
          // Log the devoir rendu (submitted assignment) data for debugging
          console.log(`Compte rendu pour le devoir ${devoir.idDevoir}:`, devoirRendu);
  
          // Check if the note exists and update the status for the devoir
          if (devoirRendu.note !== null && devoirRendu.note !== undefined) {
            this.devoirNoteStatus[devoir.idDevoir] = true;  // Mark as true if a note exists
          } else {
            this.devoirNoteStatus[devoir.idDevoir] = false;  // Mark as false if no note
          }
  
          // Assign the fetched note and feedback (commentaire)
          this.evaluationNote = devoirRendu.note;  // Assuming 'note' is part of the devoir rendu data
          this.evaluationcommentaire = devoirRendu.commentaire || '';  // Assuming 'commentaire' is part of the devoir rendu data
  
          // Show the popup with the evaluation details
          this.isPopupVisibleeval = true;
        },
        (error) => {
          console.error('Erreur lors de la récupération du compte rendu:', error);
          // Optionally, handle errors here (show an error message)
        }
      );
  }
  

  closePopupeval(): void {
    this.isPopupVisibleeval = false;  // Masquer la popup
    window.close();
  }
  
  


  isDateExpired(dateLimite: any): boolean {
    const currentDate = new Date();
    currentDate.setHours(0, 0, 0, 0); // Réinitialiser l'heure à 00:00:00
    const limiteDate = new Date(dateLimite);
    limiteDate.setHours(0, 0, 0, 0); // Réinitialiser l'heure à 00:00:00
  
    console.log('Date actuelle:', currentDate);
    console.log('Date limite du devoir:', limiteDate);
  
    const isExpired = limiteDate < currentDate;
    console.log('Est-ce que la date limite est dépassée ? ', isExpired);
    return isExpired;
  }



  getDevoirRendu(idDevoir: number, email: string): void {
    this.devoirRService.getDevoirRendubyetudiantdevoir(idDevoir, email).subscribe(
      (devoirRendu) => {
        // Display the report in the console
        console.log(`Compte rendu pour le devoir ${idDevoir}:`, devoirRendu);

        // Check if a note exists and update the devoirNoteStatus
        if (devoirRendu.note !== null && devoirRendu.note !== undefined) {
          this.devoirNoteStatus[idDevoir] = true;  // Set to true if note exists
        } else {
          this.devoirNoteStatus[idDevoir] = false; // Set to false if no note
        }
      },
      (error) => {
        console.error('Erreur lors de la récupération du compte rendu:', error);
      }
    );
  }

  /*sur(iddvr: number) {
    const email = localStorage.getItem("username");  // Récupérer l'email de l'étudiant depuis le localStorage
  
    if (email) {  // Vérifier si l'email est défini
      this.devoirRService.getDevoirRendubyetudiantdevoir(iddvr, email).subscribe(
        (devoir) => {
          // Vérifier si la note est définie et différente de 0
          if (devoir.note !== null && devoir.note !== undefined && devoir.note !== 0) {
            console.log("Le compte rendu a une note.");
            return true;  // Retourner true si la note est présente
          } else {
            console.log("Le compte rendu n'a pas de note.");
            return false;  // Retourner false si la note est absente ou zéro
          }
        },
        (error) => {
          console.error("Erreur lors de la récupération du devoir:", error);
        }
      );
    } else {
      console.log("Email non trouvé dans le localStorage.");
    }
  }
  */
  
  ngDoCheck(): void {
    const currentRoute = this.router.url;
    const role = localStorage.getItem('role');
    this.test = role !== 'etudiant';
  }

  confirm() {
    this.confirmationService.confirm({
      header: 'Confirmation',
      message: 'Please confirm to proceed moving forward.',
      acceptIcon: 'pi pi-check mr-2',
      rejectIcon: 'pi pi-times mr-2',
      rejectButtonStyleClass: 'p-button-sm',
      acceptButtonStyleClass: 'p-button-outlined p-button-sm',
      accept: () => {
        this.messageService.add({ severity: 'info', summary: 'Confirmed', detail: 'You have accepted', life: 3000 });
      },
      reject: () => {
        this.messageService.add({ severity: 'error', summary: 'Rejected', detail: 'You have rejected', life: 3000 });
      }
    });
  }

  getDocuments() {
    this.coursService.getDocumentsByCourId(this.courId).subscribe(
      (documents) => this.documentsList = documents,
      (error) => console.error('Erreur lors de la récupération des documents :', error)
    );
  }

  viewDocument(documentId: number) {
    this.coursService.downloadDocument(documentId).subscribe(
      (blob) => {
        const url = window.URL.createObjectURL(blob);
        const iframe = document.createElement('iframe');
        iframe.style.display = 'none';
        iframe.src = url;
        document.body.appendChild(iframe);
        iframe.onload = () => {
          if (iframe.contentWindow) iframe.contentWindow.print();
        };
      },
      (error) => console.error('Erreur lors de la consultation du document :', error)
    );
  }

  

  showPopup(id: number) {
    if (this.role === "enseignant") {
      this.router.navigate(['/DevoirsRenduEnseignant']);
    } else {
      localStorage.setItem("idDevoir", id.toString());
      // Trouver le devoir correspondant par ID
      this.selectedDevoir = this.devoirs.find(devoir => devoir.idDevoir === id) || null;

      if (this.selectedDevoir) {
        this.devoirRService.checkDevoirRendu(id, localStorage.getItem("username")!)
          .subscribe((r) => this.rendu = true);
        this.isPopupVisible = true;
      }
    }
  }

  onFileChange(event: Event) {
    const input = event.target as HTMLInputElement;
    if (input.files && input.files.length) {
      // Réinitialiser les listes à chaque changement de fichiers
      this.pdfFiles = [];
      this.sanitizedUrls = [];
  
      for (let i = 0; i < input.files.length; i++) {
        const file = input.files[i];
  
        // Ajouter le fichier à la liste pdfFiles
        this.pdfFiles.push(file);
  
        // Créer une URL avec URL.createObjectURL pour le fichier
        const fileUrl = URL.createObjectURL(file);
  
        // Sécuriser l'URL du fichier
        const sanitizedUrl = this.sanitizer.bypassSecurityTrustResourceUrl(fileUrl);
  
        // Ajouter l'URL sécurisée à la liste des URLs à afficher dans l'iframe
        this.sanitizedUrls.push(sanitizedUrl);
      }
  
      console.log(this.pdfFiles);            // Afficher les fichiers
      console.log(this.sanitizedUrls);       // Afficher les URLs sécurisées
    }
  }
// Méthode pour fermer le PDF dans la popup
closePDF() {
  // Logique pour fermer ou masquer l'iframe PDF
  this.isPopupVisible = false; // Par exemple, vous pouvez mettre une variable pour fermer le popup
  window.history.back();
}
  

  closePopup() {
    this.isPopupVisible = false;
    this.rendu = false;
    this.selectedDevoir = null;  // Réinitialiser le devoir sélectionné
  }

  downloadPDF(devoirId: number): void {
    this.devoirService.downloadDevoirPDF(devoirId).subscribe((blob: Blob) => {
      const url = window.URL.createObjectURL(blob);
      const a = document.createElement('a');
      a.href = url;
      a.download = `devoir_${devoirId}.pdf`;
      a.click();
    });
  }

  downloadRenduPDF(): void {
    this.devoirRService.downloadDevoirRenduPDF(+localStorage.getItem("idDevoir")!, localStorage.getItem("username")!)
      .subscribe((blob: Blob) => {
        const url = window.URL.createObjectURL(blob);
        const a = document.createElement('a');
        a.href = url;
        a.download = `rendu_${localStorage.getItem("idDevoir")}.pdf`;
        a.click();
      });
  }

  onSubmit(f: NgForm) {
    this.subbmited = true;
    if (f.invalid) return;
    this.addDevoirRendu(f);
  }

  addDevoirRendu(f: NgForm) {
    if (this.selectedDevoir) {
      // Si maxDocuments est null, on lui donne une valeur par défaut, ici 5.
      this.maxFiles = this.selectedDevoir?.maxDocuments ?? 5;  // 5 comme valeur par défaut
      console.log("maxFiles:", this.maxFiles);  // Vérification de la valeur
  
      if (this.pdfFiles.length > this.maxFiles) {
        alert(`Vous ne pouvez pas télécharger plus de ${this.maxFiles} fichiers.`);
        return;
      }
    } else {
      console.error('Le devoir sélectionné est introuvable.');
    }
  
    const formData = new FormData();
    if (this.pdfFiles && this.pdfFiles.length > 0) {
      for (let i = 0; i < this.pdfFiles.length; i++) {
        formData.append('pdfs', this.pdfFiles[i], this.pdfFiles[i].name);
      }
      this.devoirRService.addDevoirRendu(formData, +localStorage.getItem("idDevoir")!, localStorage.getItem("username")!)
        .subscribe(() => {
          this.router.navigate(['/devoirs/list/' + localStorage.getItem("idCours")]);
          this.closePopup();
        });
    } else {
      console.error('Aucun fichier sélectionné');
    }
  }
  
  

  updateDevoirRendu(f: NgForm) {
    const formData = new FormData();
    
    // Vérifiez si des fichiers ont été sélectionnés
    if (this.pdfFiles && this.pdfFiles.length > 0) {
        // Ajouter chaque fichier à FormData
        for (let i = 0; i < this.pdfFiles.length; i++) {
            formData.append('pdfs', this.pdfFiles[i], this.pdfFiles[i].name);  // 'pdfs' est le champ attendu par le backend
        }

        // Envoyer les fichiers au backend
        this.devoirRService.updateDevoirRendu(formData, +localStorage.getItem("idDevoir")!, localStorage.getItem("username")!)
            .subscribe(() => {
                // Après la mise à jour, redirigez vers la liste des devoirs
                this.router.navigate(['/devoirs/list/' + localStorage.getItem("idCours")]);
                this.closePopup();  // Fermer le popup après la soumission
            }, error => {
                console.error('Erreur lors de la mise à jour du devoir rendu', error);
            });
    } else {
        console.error('Aucun fichier sélectionné');
    }
}


  deleteDevoirRendu() {
    this.devoirRService.deleteDevoirRendu(+localStorage.getItem("idDevoir")!, localStorage.getItem("username")!)
      .subscribe(() => {
        this.router.navigate(['/devoirs/list/' + localStorage.getItem("idCours")]);
        this.closePopup();
        this.ngOnInit();
        this.rendu = false;
      });
  }

  getCourseCode() {
    this.coursService.getCoursById(this.courId).subscribe(
      (course) => {
        if (course) this.courseCode = course.code;
        this.courseNom=course.nom;
      },
      (error) => console.error('Erreur lors de la récupération du code du cours :', error)
    );
  }

  inviteStudent() {
    this.coursService.inviteStudentByEmail(this.courseCode, this.studentEmail).subscribe(
      (response) => {
        console.log('Réponse du serveur :', response); 
        this.invitationStudentSuccess = "L'étudiant a été invité avec succès";
        this.invitationStudentMessageClass = 'success-message';
        this.studentEmail = '';
      },
      (error) => {
        console.error('Erreur lors de l\'invitation de l\'étudiant :', error); 
        if (error.status === 400) {
          this.invitationStudentError = "Demande incorrecte. Vérifiez l'email et le code du cours.";
        } else if (error.status === 500) {
          this.invitationStudentError = "Erreur serveur. Veuillez réessayer plus tard.";
        } else {
          this.invitationStudentSuccess = "L'étudiant a été invité avec succès";
          this.invitationStudentMessageClass = 'success-message';
        }
        this.invitationStudentMessageClass = 'error-message';
      }
    );
  }
  
  
  inviteTeacher() {
    this.coursService.inviteTeacherByEmail(this.courseCode, this.teacherEmail).subscribe(
      (response) => {
        console.log('Réponse du serveur :', response);
        this.invitationTeacherSuccess = "L'enseignant a été invité avec succès";
        this.invitationTeacherMessageClass = 'success-message';
        this.invitationTeacherError = ""; 
        this.teacherEmail = '';
      },
      (error) => {
        console.error('Erreur lors de l\'invitation de l\'enseignant :', error);
        this.invitationTeacherSuccess = ""; 
        if (error.status === 400) {
          this.invitationTeacherError = "Demande incorrecte. Vérifiez l'email et le code du cours.";
        } else if (error.status === 500) {
          this.invitationTeacherError = "Erreur serveur. Veuillez réessayer plus tard.";
        } else {
          this.invitationTeacherSuccess = "L'enseignant a été invité avec succès";
          this.invitationStudentMessageClass = 'success-message';
        }
        this.invitationTeacherMessageClass = 'error-message';
      }
    );
  }
  
  
}
