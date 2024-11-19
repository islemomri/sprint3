package com.project.app.services.jwt;

import java.util.List;

import com.project.app.dto.SousGroupeDTO;

import com.project.app.models.SousGroupe;

public interface ISousGroupeService {

	public SousGroupe addSousGroupe(SousGroupeDTO sousGroupeDTO,int idCours);
	public List<SousGroupe> getAllSousGroupes(Integer idCours);
	 public SousGroupe modifierSousGroupe(Integer idSousGroupe, SousGroupeDTO sousGroupeDTO);
	 public void deleteSousGroupe(Integer id);
	 public SousGroupe addEtudiantSousGroupe(Integer id,String email);
}