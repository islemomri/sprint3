package com.project.app.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Lob;
import lombok.Data;

@Data
public class DevoirRenduDTO {
	private Long idDevoirRendu;
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
	 
	 
}
