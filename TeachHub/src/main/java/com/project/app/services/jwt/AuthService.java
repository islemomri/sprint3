package com.project.app.services.jwt;

import com.project.app.dto.SignUpRequest;
import com.project.app.models.Enseignant;
import com.project.app.models.Etudiant;

public interface AuthService {
	Etudiant createEtudiant(SignUpRequest signupRequest);
    Enseignant createEnseignant(SignUpRequest signupRequest);
}
