import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { AuthService } from 'src/app/auth/service/auth.service';
import { IDevoirRendu } from '../model/idevoir-rendu';
import { catchError, map, Observable, throwError } from 'rxjs';
import { EvaluationDTO } from '../model/evaluation-dto';

const BASE_URL = ["http://localhost:9099/"];
@Injectable({
  providedIn: 'root'
})
export class DevoirRenduService {

  
  devoirs!:IDevoirRendu[];
  constructor(private http:HttpClient,private service:AuthService) { }
  headers= this.service.createAuthorizationHeader()
  getAllDevoirsRendu(idDevoirs:number): Observable<IDevoirRendu[]> {
    return this.http.get<IDevoirRendu[]>(BASE_URL + "DevoirRendu/"+idDevoirs, { headers: this.headers! });
  }
  addDevoirRendu(formData: FormData, id: number, email: string): Observable<any> {
    // Affichage du contenu de FormData dans la console
    formData.forEach((value, key) => {
      console.log(`FormData contient la clé: ${key} avec la valeur:`, value);
    });
  
    // Envoi de la requête HTTP
    return this.http.post<any>(BASE_URL + 'addDevoirRendu/' + id + '/' + email, formData, { headers: this.headers! });
  }
  
  downloadDevoirRenduPDF(devoirId: number,email:string): Observable<Blob> {
    return this.http.get(BASE_URL + `devoirRendu/download/${devoirId}/${email}`, { responseType: 'blob' });
  }
  updateDevoirRendu(formData: FormData , id : number,email:string):Observable<IDevoirRendu>{
    return this.http.put<IDevoirRendu>(BASE_URL+"updateDevoirRendu/"+id+"/"+email,formData,{headers:this.headers!})
  }
  deleteDevoirRendu(id : number,email:string):Observable<string>{
    return this.http.delete(BASE_URL+"deleteDevoirRendu/"+id+"/"+email,{headers:this.headers!, responseType: 'text'})
  }
 /* getDevoirRenduById(id: number): Observable<IDevoirRendu | null> {
    return this.getAllDevoirsRendu(+localStorage.getItem("idDevoir")!).pipe(
      map(devoir => {
        this.devoirs=devoir;
        return this.devoirs.find( d=> d.idDevoirRendu === id)||null;
       
      })
    );
  }*/
    getDevoirRenduById(idDevoirRendu: number): Observable<IDevoirRendu | null> {
      // Utilisez la concaténation de chaînes
      const url = BASE_URL + 'devoirRendu/' + idDevoirRendu;
      return this.http.get<IDevoirRendu | null>(url); // Retourne l'objet ou null si non trouvé
    }
    
  checkDevoirRendu(id: number,email:string){
    return this.http.get(BASE_URL + `devoirRendu/${id}/${email}`,{headers:this.headers!, responseType: 'text'});
  }

  evaluerDevoir(evaluation: EvaluationDTO): Observable<IDevoirRendu> {
    return this.http.post<IDevoirRendu>(BASE_URL + "evaluerDevoir", evaluation, { headers: this.headers! });
  }

  getEvaluation(idDevoir: number, email: string): Observable<EvaluationDTO> {
    return this.http.get<EvaluationDTO>(BASE_URL + `devoirRendu/evaluation/${idDevoir}/${email}`);
  }
  
  getDevoirRenduById1(idDevoirRendu: number, email: string): Observable<IDevoirRendu | null> {
    return this.http.get<IDevoirRendu>(BASE_URL+`devoirRendu/${idDevoirRendu}/${email}`); }


    getDevoirRendubyetudiantdevoir(idDevoir: number, email: string): Observable<IDevoirRendu> {
      console.log(`Récupération du devoir rendu pour l'étudiant ${email}, devoir ${idDevoir}`);
      return this.http.get<IDevoirRendu>(`http://localhost:9099/devoirRendu/${idDevoir}/${email}`);
    }

}