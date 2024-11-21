package com.project.app.models;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Data
@Entity
public class DevoirRendu {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idDevoirRendu;
	@JsonIgnore
	@ElementCollection
	@Lob  
	private List<byte[]> pdfs;  
	 @ManyToOne
	 @JoinColumn(name = "id_devoir", nullable = false)
	 private Devoir devoir;
	 private String  commentaire;
	 private Float note;
	 
	 
	 @ManyToOne(fetch=FetchType.EAGER)
	 @JoinColumn(name="etudiant_id")
	 private Etudiant etudiant;

	
	public Long getIdDevoirRendu() {
		return idDevoirRendu;
	}

	public void setIdDevoirRendu(Long idDevoirRendu) {
		this.idDevoirRendu = idDevoirRendu;
	}
	public List<byte[]> getPdfs() {
		return pdfs;
	}

	public void setPdfs(List<byte[]> pdfs) {
		this.pdfs = pdfs;
	}

	

	public Devoir getDevoir() {
		return devoir;
	}

	public void setDevoir(Devoir devoir) {
		this.devoir = devoir;
	}

	public Etudiant getEtudiant() {
		return etudiant;
	}

	public void setEtudiant(Etudiant etudiant) {
		this.etudiant = etudiant;
	}

	public String getCommentaire() {
		return commentaire;
	}

	public void setCommentaire(String commentaire) {
		this.commentaire = commentaire;
	}

	public Float getNote() {
		return note;
	}

	public void setNote(Float note) {
		this.note = note;
	}

	
	
	 

}
