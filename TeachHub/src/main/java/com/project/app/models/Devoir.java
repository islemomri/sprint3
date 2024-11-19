package com.project.app.models;



import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.Future;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Data
@Entity
public class Devoir {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idDevoir;

    private String typedevoir;  
    private String description;

    private float ponderation;

    private String bareme;
    
    //@Future(message = "La date limite doit être dans le futur")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date dateLimite;

    private String statut;
  
    @ManyToOne
    @JoinColumn(name = "id_cours", nullable = false)
    private Cour cours;
    
    @ManyToMany
    @JoinTable(name = "devoir_sousgroupe",
               joinColumns = @JoinColumn(name = "devoir_id"),
               inverseJoinColumns = @JoinColumn(name = "sousgroupe_id"))
    private List<SousGroupe> sousGroupes = new ArrayList<>();
    
    @Lob
    private byte[] pdf;  // Champ pour stocker le fichier PDF

    
    private Integer maxdocuments;
	 
	 public Integer getMaxDocuments() {
	        return maxdocuments;
	    }

	    public void setMaxDocuments(Integer maxDocuments) {
	        this.maxdocuments = maxDocuments;
	    }
	    
    // Getters et setters

    public byte[] getPdf() {
        return pdf;
    }

    public void setPdf(byte[] pdf) {
        this.pdf = pdf;
    }

	public Long getIdDevoir() {
		return idDevoir;
	}

	public void setIdDevoir(Long idDevoir) {
		this.idDevoir = idDevoir;
	}

	
	public String getTypedevoir() {
		return typedevoir;
	}

	public void setTypedevoir(String typedevoir) {
		this.typedevoir=typedevoir;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public float getPonderation() {
		return ponderation;
	}

	public void setPonderation(float ponderation) {
		this.ponderation = ponderation;
	}

	public String getBareme() {
		return bareme;
	}

	public void setBareme(String bareme) {
		this.bareme = bareme;
	}

	public Date getDateLimite() {
		return dateLimite;
	}

	public void setDateLimite(Date dateLimite) {
		this.dateLimite = dateLimite;
	}

	public String getStatut() {
		return statut;
	}

	public void setStatut(String statut) {
		this.statut = statut;
	}

	public Cour getCours() {
		return cours;
	}

	public void setCours(Cour cours) {
		this.cours = cours;
	}

	public List<SousGroupe> getSousGroupes() {
		return sousGroupes;
	}

	public void setSousGroupes(List<SousGroupe> sousGroupes) {
		this.sousGroupes = sousGroupes;
	}

	


	

}
