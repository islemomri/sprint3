import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from '../service/auth.service';
import { inject } from '@angular/core';

export const teachHubGuard: CanActivateFn = (route, state) => {
  const authService = inject(AuthService);
  const router = inject(Router);
  if(authService.isAuthenticated()){
    if(route.url[0]!=undefined){
      if((localStorage.getItem("role")=="etudiant" && (route.url[0].path=="cours/add") || route.url[0].path=="cours/update" || route.url[0].path=="devoir/add" || route.url[0].path=="devoir/list" || route.url[0].path=="sous-groupe")){
 
  return false;}
  else{
    return true;
  }
}else{
  router.navigate(['/auth/login']);
  return false;
}}else{
  return false;
}

}
