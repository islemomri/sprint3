package com.project.app.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Lob;
import lombok.Data;

@Data
public class DevoirRenduDTO {
	private Long idDevoirRendu;
	 private String  commentaire;
	 private Float note;
	@ElementCollection
	@Lob  
	private List<byte[]> pdfs; 
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
