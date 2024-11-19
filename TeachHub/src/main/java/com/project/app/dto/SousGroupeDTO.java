package com.project.app.dto;

import java.util.List;

import lombok.Data;

@Data
public class SousGroupeDTO {
	
	private Integer idSousGroupe;
    
    private String nom;

    private List<String> etudiants;

	public Integer getIdSousGroupe() {
		return idSousGroupe;
	}

	public void setIdSousGroupe(Integer idSousGroupe) {
		this.idSousGroupe = idSousGroupe;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public List<String> getEtudiants() {
		return etudiants;
	}

	public void setEtudiants(List<String> etudiants) {
		this.etudiants = etudiants;
	}
    
}
