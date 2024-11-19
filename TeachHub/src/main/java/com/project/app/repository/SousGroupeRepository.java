package com.project.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.app.models.SousGroupe;

@Repository
public interface SousGroupeRepository extends JpaRepository<SousGroupe, Integer>{

}
