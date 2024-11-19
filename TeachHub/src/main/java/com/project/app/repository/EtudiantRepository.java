package com.project.app.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.app.models.Etudiant;
import java.util.List;


@Repository
public interface EtudiantRepository extends JpaRepository<Etudiant, Long>{
	Optional<Etudiant> findByEmail(String email);
	boolean existsByEmail(String email);
	
}
