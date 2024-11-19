package com.project.app.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.app.dto.SignUpRequest;
import com.project.app.models.Enseignant;
import com.project.app.models.Etudiant;
import com.project.app.services.jwt.AuthService;



@RestController

public class SignUpController {
	
private final AuthService authService;
	
	@Autowired
	public SignUpController(AuthService authService) {
		this.authService=authService;
	}
	
	@PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody SignUpRequest signUpRequest) {
        if ("ROLE_ETUDIANT".equals(signUpRequest.getRole())) {
            Etudiant etudiant = authService.createEtudiant(signUpRequest);
            if (etudiant != null) {
                return ResponseEntity.status(HttpStatus.CREATED).body(etudiant);
            }
        } else if ("ROLE_ENSEIGNANT".equals(signUpRequest.getRole())) {
            Enseignant enseignant = authService.createEnseignant(signUpRequest);
            if (enseignant != null) {
                return ResponseEntity.status(HttpStatus.CREATED).body(enseignant);
            }
        }
        
        return ResponseEntity.badRequest().body("L'utilisateur existe déjà ou les informations sont incorrectes");
    }

}
