import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { AuthService } from 'src/app/auth/service/auth.service';
import { ISousGroupe } from '../model/isous-groupe';
import { map, Observable } from 'rxjs';


const BASE_URL = ["http://localhost:9099/"];
@Injectable({
  providedIn: 'root'
})
export class SousGroupeService {
  sousGroupes!:ISousGroupe[];
  constructor(private http:HttpClient,private service:AuthService) { }
  headers= this.service.createAuthorizationHeader()
  getAllSousgroupes(idCours: number): Observable<ISousGroupe[]> {
    // Vérifier si les en-têtes sont bien initialisés
    console.log(this.headers); // Pour vérifier la valeur de `this.headers`
  
    // Si `this.headers` est `undefined`, vous devez initialiser cet en-tête
    if (!this.headers) {
      this.headers = new HttpHeaders().set('Authorization', 'Bearer ' + this.service.getToken());
    }
  
    return this.http.get<ISousGroupe[]>(BASE_URL + "sousgroupe/" + idCours, { headers: this.headers });
  }
  
  addSousgroupe(sg: ISousGroupe, id: number): Observable<ISousGroupe> {
    console.log(this.headers); // Pour vérifier la valeur de `this.headers`
  
    // Si `this.headers` est `undefined`, vous devez initialiser cet en-tête
    if (!this.headers) {
      this.headers = new HttpHeaders().set('Authorization', 'Bearer ' + this.service.getToken());
    }
    return this.http.post<ISousGroupe>(BASE_URL + 'addsousgroupe/' + id, sg, { headers: this.headers! });
  }
  updateSousgroupe(sg: ISousGroupe , id : number):Observable<ISousGroupe>{
    console.log(this.headers); // Pour vérifier la valeur de `this.headers`
  
    // Si `this.headers` est `undefined`, vous devez initialiser cet en-tête
    if (!this.headers) {
      this.headers = new HttpHeaders().set('Authorization', 'Bearer ' + this.service.getToken());
    }
    return this.http.put<ISousGroupe>(BASE_URL+"updatesousgroupe/"+id,sg,{headers:this.headers!})
  }
  deleteSousgroupe(id : number):Observable<string>{
    console.log(this.headers); // Pour vérifier la valeur de `this.headers`
  
    // Si `this.headers` est `undefined`, vous devez initialiser cet en-tête
    if (!this.headers) {
      this.headers = new HttpHeaders().set('Authorization', 'Bearer ' + this.service.getToken());
    }
    return this.http.delete(BASE_URL+"deletesousgroupe/"+id,{headers:this.headers!, responseType: 'text'})
  }
  getSousgroupeById(id: number): Observable<ISousGroupe | null> {

    return this.getAllSousgroupes(+localStorage.getItem("idCours")!).pipe(
      map(sg => {
        this.sousGroupes=sg;
        return this.sousGroupes.find( s=> s.idSousGroupe === id)||null;
       
      })
    );
  }
  addEtudiantSousGroupe(idSousGroupe: number, email: string): Observable<any> {
    const headers = this.service.createAuthorizationHeader(); // Make sure this line is present
    return this.http.post<any>(`${BASE_URL}addEtudiantSousgroupe/${idSousGroupe}/${email}`, {}, { headers });
  }
  
}
