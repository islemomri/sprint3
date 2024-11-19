import { ChangeDetectorRef, Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from './auth/service/auth.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'TeachHub';
  isMenuVisible!: boolean;
  dev!: boolean;
  test!: boolean;

  constructor(private router: Router, private serviceAuth: AuthService, private cdr: ChangeDetectorRef) {}

  
  ngDoCheck(): void {
    let currentroute = this.router.url;
    let role = localStorage.getItem('role');
    let jwt = localStorage.getItem('jwt');

    // Vérifier si l'utilisateur est un étudiant
    if (role == 'etudiant') {
      this.test = false;
    } else if (currentroute == '/cours') {
      this.test = true;
    } else {
      this.test = false;
    }

    // Vérification de l'affichage du menu
    if (currentroute === '/auth/login' || currentroute === '/auth/register/enseignant' || currentroute === '/auth/register/etudiant' || currentroute === '/auth') {
      this.isMenuVisible = false;
    } else {
      this.isMenuVisible = true;
    }

    // Vérifier si on est sur la page de liste des devoirs
    this.dev = (currentroute.startsWith('/devoirs/list') || currentroute.startsWith('/devoirRendu/list')) && role == 'enseignant';
  }
}
