package com.project.app.models;


import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Data;
@Data
@Entity
public class Cour {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	
    private Integer idCours;

    private String nom;
    private float coefficient;
    private int credits;
    private String code;

    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="enseignant_id")
    @JsonIgnore
    private Enseignant enseignant;
    
    @ManyToMany
    @JoinTable(name = "course_student",joinColumns = @JoinColumn(name = "course_id"),inverseJoinColumns = @JoinColumn(name = "student_id")
    )
    @JsonIgnore
    private Set<Etudiant> students = new HashSet<>();
    
    @ManyToMany
    @JoinTable(name = "course_teachers",joinColumns = @JoinColumn(name = "course_id"),inverseJoinColumns = @JoinColumn(name = "teacher_id")
    )
    @JsonIgnore
    private Set<Enseignant> invitedTeachers = new HashSet<>();
    
    @OneToMany
    (mappedBy = "cour", cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<Document> documents = new HashSet<>();
    
    
    private String methodeCalcul;
    public String getMethodeCalcul() {
		return methodeCalcul;
	}

	public void setMethodeCalcul(String methodeCalcul) {
		this.methodeCalcul = methodeCalcul;
	}

	
    
    public Cour() {
        super();
    }

	public Cour(int i, String string, float f, int j) {
		this.idCours=idCours;
		this.nom=string;
		this.coefficient=f;
		this.credits=j;
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

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Enseignant getEnseignant() {
		return enseignant;
	}

	public void setEnseignant(Enseignant enseignant) {
		this.enseignant = enseignant;
	}

	public Set<Etudiant> getStudents() {
		return students;
	}

	public void setStudents(Set<Etudiant> students) {
		this.students = students;
	}

	public Set<Enseignant> getInvitedTeachers() {
		return invitedTeachers;
	}

	public void setInvitedTeachers(Set<Enseignant> invitedTeachers) {
		this.invitedTeachers = invitedTeachers;
	}

	public Set<Document> getDocuments() {
		return documents;
	}

	public void setDocuments(Set<Document> documents) {
		this.documents = documents;
	}

	

}
