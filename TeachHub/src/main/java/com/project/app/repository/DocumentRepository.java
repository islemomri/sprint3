package com.project.app.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.app.models.Cour;
import com.project.app.models.Document;
@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {
	List<Document> findByCour(Cour cour);
	Optional<Document> findById(Long id);
	

}
