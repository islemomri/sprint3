package com.project.app.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.project.app.models.Cour;
import com.project.app.models.Enseignant;



@Repository
public interface CourRepository extends JpaRepository <Cour,Integer>{
	List<Cour> findByInvitedTeachersContains(Enseignant enseignant);
	 List<Cour> findByEnseignant_Id(Long enseignantId);
	 @Query("SELECT c FROM Cour c JOIN c.students s WHERE s.id = :etudiantId")
	    List<Cour> findByStudents_Id(@Param("etudiantId") Long etudiantId);
	 Cour findByCode(String code);
}
