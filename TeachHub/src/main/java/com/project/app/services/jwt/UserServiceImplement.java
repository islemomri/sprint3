package com.project.app.services.jwt;


import java.util.Collections;
import java.util.List;

import java.util.Optional;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.project.app.models.Enseignant;
import com.project.app.models.Etudiant;
import com.project.app.repository.EnseignantRepository;
import com.project.app.repository.EtudiantRepository;



@Service
public class UserServiceImplement implements UserDetailsService {
	
	private final EtudiantRepository etudiantRepository;
    private final EnseignantRepository enseignantRepository;
    
    @Autowired
    public UserServiceImplement(EtudiantRepository etudiantRepository, EnseignantRepository enseignantRepository) {
        this.etudiantRepository = etudiantRepository;
        this.enseignantRepository = enseignantRepository;
    }
    
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<Etudiant> etudiantOpt = etudiantRepository.findByEmail(email);
        if (etudiantOpt.isPresent()) {
            Etudiant etudiant = etudiantOpt.get();
            List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_ETUDIANT"));
            return new User(etudiant.getEmail(), etudiant.getMotDePasse(), authorities);
        }
        

        Optional<Enseignant> enseignantOpt = enseignantRepository.findByEmail(email);
        if (enseignantOpt.isPresent()) {
            Enseignant enseignant = enseignantOpt.get();
            List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_ENSEIGNANT"));
            return new User(enseignant.getEmail(), enseignant.getMotDePasse(), authorities);
        }

        throw new UsernameNotFoundException("Utilisateur non trouvé avec l'email: " + email);
    }

    public Long getIdUser(String email) {
    	Optional<Etudiant> etudiantOpt = etudiantRepository.findByEmail(email);
    	Long id=null;
        if (etudiantOpt.isPresent()) {
        	id=etudiantOpt.get().getId();
        }else {
        Optional<Enseignant> enseignantOpt = enseignantRepository.findByEmail(email);
        if (enseignantOpt.isPresent()) {
        	id=enseignantOpt.get().getId();
        }}
        return id;
        }
    }


