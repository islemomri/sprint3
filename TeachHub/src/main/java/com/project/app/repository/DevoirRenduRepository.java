package com.project.app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.app.models.DevoirRendu;

@Repository
public interface DevoirRenduRepository extends JpaRepository <DevoirRendu, Long>{
	List<DevoirRendu> findByDevoir_IdDevoir(Integer idDevoir);

}