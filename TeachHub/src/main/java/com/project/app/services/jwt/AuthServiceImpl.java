package com.project.app.services.jwt;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.project.app.dto.SignUpRequest;
import com.project.app.models.Enseignant;
import com.project.app.models.Etudiant;
import com.project.app.repository.EnseignantRepository;
import com.project.app.repository.EtudiantRepository;


@Service
public class AuthServiceImpl implements AuthService {
	
	
	private final EtudiantRepository etudiantRepository;
    private final EnseignantRepository enseignantRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthServiceImpl(EtudiantRepository etudiantRepository, 
                           EnseignantRepository enseignantRepository, 
                           PasswordEncoder passwordEncoder) {
        this.etudiantRepository = etudiantRepository;
        this.enseignantRepository = enseignantRepository;
        this.passwordEncoder = passwordEncoder;
    }
    
    @Override
    public Etudiant createEtudiant(SignUpRequest signupRequest) {
        if (etudiantRepository.existsByEmail(signupRequest.getEmail())) {
            return null; 
        }
        Etudiant etudiant = new Etudiant();
        BeanUtils.copyProperties(signupRequest, etudiant);

        // Encoder le mot de passe
        String hashPassword = passwordEncoder.encode(signupRequest.getMotDePasse());
        etudiant.setMotDePasse(hashPassword);

        // Sauvegarde
        Etudiant createdEtudiant = etudiantRepository.save(etudiant);
        etudiant.setId(createdEtudiant.getId());
        return etudiant;
    }
    
    // Création d'un enseignant
    @Override
    public Enseignant createEnseignant(SignUpRequest signupRequest) {
        if (enseignantRepository.existsByEmail(signupRequest.getEmail())) {
            return null; // Gestion du cas où l'email est déjà utilisé
        }

        Enseignant enseignant = new Enseignant();
        BeanUtils.copyProperties(signupRequest, enseignant);

        // Encoder le mot de passe
        String hashPassword = passwordEncoder.encode(signupRequest.getMotDePasse());
        enseignant.setMotDePasse(hashPassword);

        // Sauvegarde
        Enseignant createdEnseignant = enseignantRepository.save(enseignant);
        enseignant.setId(createdEnseignant.getId());
        return enseignant;
    }
        
}
