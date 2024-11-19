package com.project.app.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.app.models.Enseignant;

@Repository
public interface EnseignantRepository extends JpaRepository<Enseignant, Long> {
	Optional<Enseignant> findByEmail(String email);
	boolean existsByEmail(String email);
	

}
