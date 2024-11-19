import { Injectable } from '@angular/core';
import { ICours } from '../model/icours';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { AuthService } from 'src/app/auth/service/auth.service';
import { catchError, map, Observable, tap, throwError } from 'rxjs';
import { ICoursDTO } from '../model/icours-dto';
import { IEnseignant } from 'src/app/auth/model/ienseignant';
const BASE_URL = ["http://localhost:9099/"];
@Injectable({
  providedIn: 'root'
})
export class CoursService {
  private apiUrl = 'http://localhost:9099/';
  cours!:ICours[];
  headers= this.service.createAuthorizationHeader()
  
  
  constructor(private http:HttpClient,private service:AuthService) { }

  getAllCours():Observable<ICours[]>{
    return this.http.get<ICours[]>(BASE_URL+"cours/"+localStorage.getItem("id"),{headers:this.headers!});
  }
  addCours(cours:ICours):Observable<ICours> {
    return this.http.post<ICours>(BASE_URL + "addcour/"+localStorage.getItem("username"),cours,{headers:this.headers!});
  }
  updateCours(cours:ICoursDTO , id : number):Observable<ICours> {
    return this.http.put<ICours>(BASE_URL + "updatecours/"+id,cours,{headers:this.headers!});
  }
  deleteCours(id: number): Observable<string> {
    return this.http.delete(BASE_URL + 'deletecours/'+ id, { headers: this.headers!, responseType: 'text' });
  }
  inviteStudentById(courseCode: string, studentId: number): Observable<string> {
    return this.http.post<string>(`${BASE_URL}${courseCode}/inviteById/${studentId}`, {}, { headers: this.headers! });
  }
  getCoursByStudentId(studentId: number): Observable<ICours[]> {
    return this.http.get<ICours[]>(`${BASE_URL}cours/etudiant/${studentId}`, { headers: this.headers! });
  }
  
  getCoursByEnseignantId(enseignantId: number): Observable<ICours[]> {
    return this.http.get<ICours[]>(`${BASE_URL}cours/enseignant/${enseignantId}`, { headers: this.headers })
      .pipe(
        catchError(this.handleError) // Gère les erreurs de manière centralisée
      );
  }

  // Gestionnaire d'erreur centralisé
  private handleError(error: HttpErrorResponse) {
    if (error.error instanceof ErrorEvent) {
      // Erreur côté client
      console.error('Une erreur côté client est survenue:', error.error.message);
    } else {
      // Erreur côté serveur
      console.error(`Erreur du serveur : ${error.status}, message : ${error.message}`);
    }
    // Renvoyer l'erreur au composant ou au service appelant
    return throwError('Une erreur est survenue. Veuillez réessayer plus tard.');
  }

  

  
  inviteStudentByEmail(courseCode: string, studentEmail: string): Observable<string> {
    return this.http.post<string>(
        `${BASE_URL}${courseCode}/inviteByEmail`, 
        { studentEmail }, // Envoyer le studentEmail dans le corps
        { headers: this.headers! }
    );
}

inviteTeacherByEmail(courseCode: string, teacherEmail: string): Observable<string> {
  return this.http.post<string>(
      `${BASE_URL}${courseCode}/inviteTeacherByEmail`, 
      { teacherEmail }, // Envoyer le studentEmail dans le corps
      { headers: this.headers! }
  );
}

getCoursById(courId: number): Observable<ICours> {
  return this.http.get<ICours>(`${BASE_URL}cours/${courId}`, { headers: this.headers! });
}

getCoursesForInvitedTeacher(teacherEmail: string): Observable<ICours[]> {
  return this.http.get<ICours[]>(`${BASE_URL}cours/invited-enseignant/${teacherEmail}`, { headers: this.headers! });
}


  uploadDocument(file: File, courId: number, enseignantId: number): Observable<any> {
    const formData: FormData = new FormData();
    formData.append('file', file, file.name);

    return this.http.post<string>(`${BASE_URL}upload/${courId}/${enseignantId}`, formData, { headers: this.headers ! });
  }

  
  getDocumentsByCourId(courId: number): Observable<any[]> {
     return this.http.get<any[]>(`${BASE_URL}cours/${courId}/documents`, { headers: this.headers ! }); 
    }

    downloadDocument(documentId: number): Observable<Blob> { 
      return this.http.get(`${BASE_URL}documents/${documentId}`, { responseType: 'blob' }); }
      
  
}
