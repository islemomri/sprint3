import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { AuthService } from '../service/auth.service';

@Component({
  selector: 'app-signup',
  templateUrl: './signup.component.html',
  styleUrls: ['./signup.component.css']
})
export class SignupComponent {
  role= this.activatedRoute.snapshot.params['role'];
  registerForm!: FormGroup;
  constructor(
    private activatedRoute:ActivatedRoute,
    private service: AuthService,
    private fb: FormBuilder,
    private router:Router
  ) { }
r!:string;
test!:boolean
  ngOnInit(): void {
    this.activatedRoute.paramMap.subscribe(params => {
      const role = params.get('role');
      console.log(role);
      if (role === 'enseignant') {
        this.r = 'ROLE_ENSEIGNANT';
        this.test = true;
      } else {
        this.r = 'ROLE_ETUDIANT';
        this.test = false;
      }

    
   
    this.registerForm = this.fb.group({
    
      email: ['', [Validators.required]],
      motDePasse: ['', [Validators.required]],
      confirmPassword: ['', [Validators.required]],
      nom:['', [Validators.required]],
      prenom :['', [Validators.required]],
      role:[this.r, [Validators.required]],
    }, { validator: this.passwordMathValidator })
  });
   
  }

  passwordMathValidator(formGroup: FormGroup) {
    const password = formGroup.get('motDePasse')?.value;
    const confirmPassword = formGroup.get('confirmPassword')?.value;
    if (password != confirmPassword) {
      formGroup.get('confirmPassword')?.setErrors({ passwordMismatch: true });
    } else {
      formGroup.get('confirmPassword')?.setErrors(null);
    }
  }

  submitForm() {
    console.log(this.registerForm!.value);
    this.service.register(this.registerForm!.value).subscribe(
      (response) => {
        console.log("Response from server: ", response);
        if (response ) {
          alert("Hello " + response.email);
          this.router.navigateByUrl("auth/login");
        }
      }
    )
  }
}
