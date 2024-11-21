package com.project.app.dto;

public class EvaluationDTO {
    private Long idDevoirRendu;
    
    private Float note;
    private String commentaire;
	public Long getIdDevoirRendu() {
		return idDevoirRendu;
	}
	public void setIdDevoirRendu(Long idDevoirRendu) {
		this.idDevoirRendu = idDevoirRendu;
	}
	public Float getNote() {
		return note;
	}
	public void setNote(Float note) {
		this.note = note;
	}
	public String getCommentaire() {
		return commentaire;
	}
	public void setCommentaire(String commentaire) {
		this.commentaire = commentaire;
	}
    

    
}