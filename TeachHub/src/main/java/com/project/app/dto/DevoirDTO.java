package com.project.app.dto;

import java.util.Date;

import javax.validation.constraints.Future;

import com.project.app.models.Typedevoir;


public class DevoirDTO {
	   private Long idDevoir;
	    private String typedevoir;  
	    private String description;

	    private float ponderation;

	    private String bareme;
	    
	    @Future(message = "La date limite doit être dans le futur")
	    private Date dateLimite;

	    private String statut;
	    private byte[] pdf;  // Champ pour stocker le fichier PDF
	    private String sousGroupes;
	    
	    private Integer maxdocuments;
		 
		 public Integer getMaxDocuments() {
		        return maxdocuments;
		    }

		    public void setMaxDocuments(Integer maxdocuments) {
		        this.maxdocuments = maxdocuments;
		    }
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
			this.typedevoir = typedevoir;
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
		
		public String getSousGroupes() {
			return sousGroupes;
		}


		public void setSousGroupes(String sousGroupes) {
			this.sousGroupes = sousGroupes;
		}
	    
}
