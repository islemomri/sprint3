import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AuthService } from '../service/auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  loginForm!: FormGroup;
  constructor(
    private service: AuthService,
    private fb: FormBuilder,
    private router: Router
  ) { }
  
  ngOnInit(): void {
    this.loginForm = this.fb.group({
      email: ['', Validators.required],
      motDePasse: ['', Validators.required],
    })
  }
  
  submitForm() {
    this.service.login(this.loginForm.value).subscribe(
      (response) => {
        console.log(response);
        if (response.jwt != null) {
          localStorage.setItem("role",response.role);
          
          localStorage.setItem("username",this.loginForm.value.email);
          localStorage.setItem("email",this.loginForm.value.email);
          console.log(response.role);
          localStorage.setItem("id",response.id)
          
          alert("Hello, Your token is " + response.jwt);
          const jwtToken = response.jwt;
          localStorage.setItem('jwt', jwtToken);
          this.router.navigateByUrl("cours");
      
      }},
      (error)=>{
        alert("Email ou mot de passe incorrect ! " );
      }
      )
    
  }
}