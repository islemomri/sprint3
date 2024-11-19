package com.project.app.dto;

public class CourDTO {

	private Integer idCours;

    private String nom;

    private float coefficient;

    private int credits;
    private String code;

    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }

    private String methodeCalcul;
    public String getMethodeCalcul() {
		return methodeCalcul;
	}
	public void setMethodeCalcul(String methodeCalcul) {
		this.methodeCalcul = methodeCalcul;
	}
	public CourDTO(Integer idCours, String nom, float coefficient, int credits) {
        this.idCours = idCours;
        this.nom = nom;
        this.coefficient = coefficient;
        this.credits = credits;
    }
	public Integer getIdCours() {
		return idCours;
	}

	public void setIdCours(Integer idCours) {
		this.idCours = idCours;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public float getCoefficient() {
		return coefficient;
	}

	public void setCoefficient(float coefficient) {
		this.coefficient = coefficient;
	}

	public int getCredits() {
		return credits;
	}

	public void setCredits(int credits) {
		this.credits = credits;
	}
    
}
