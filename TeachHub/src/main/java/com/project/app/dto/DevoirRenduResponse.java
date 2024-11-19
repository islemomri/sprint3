package com.project.app.dto;

import java.util.List;

import lombok.Data;

@Data
public class DevoirRenduResponse {
	
	private Long idDevoirRendu;
	private List<byte[]> pdfs;
	 private String email;
	public Long getIdDevoirRendu() {
		return idDevoirRendu;
	}
	public void setIdDevoirRendu(Long idDevoirRendu) {
		this.idDevoirRendu = idDevoirRendu;
	}
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public List<byte[]> getPdfs() {
		return pdfs;
	}
	public void setPdfs(List<byte[]> pdfs) {
		this.pdfs = pdfs;
	}
	 
}
