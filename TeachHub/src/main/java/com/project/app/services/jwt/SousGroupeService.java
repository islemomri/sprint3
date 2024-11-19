package com.project.app.services.jwt;

import java.util.ArrayList;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import com.project.app.dto.SousGroupeDTO;
import com.project.app.models.Cour;

import com.project.app.models.Etudiant;
import com.project.app.models.SousGroupe;
import com.project.app.repository.CourRepository;
import com.project.app.repository.EtudiantRepository;
import com.project.app.repository.SousGroupeRepository;


@Service
public class SousGroupeService implements ISousGroupeService{

	@Autowired
    private SousGroupeRepository sousGroupeRepository;
	@Autowired
    private CourRepository coursRepository;
	@Autowired
    private EtudiantRepository etudiantRepository;
	
	public SousGroupe mapToEntity(SousGroupeDTO SousGroupeDTO) {
		SousGroupe sg=new SousGroupe();
		sg.setIdSousGroupe(SousGroupeDTO.getIdSousGroupe());
		sg.setNom(SousGroupeDTO.getNom());
		List<Etudiant> list=new ArrayList<>();
		for(String e:SousGroupeDTO.getEtudiants()) {
			Etudiant et=this.etudiantRepository.findByEmail(e).orElseThrow();
			list.add(et);
		}
		sg.setEtudiants(list);
		return sg;
	}

	@Override
	public SousGroupe addSousGroupe(SousGroupeDTO sousGroupeDTO, int idCours) {
		
		SousGroupe sg=this.mapToEntity(sousGroupeDTO);
		for(Etudiant e:sg.getEtudiants()) {
			for(SousGroupe s: this.getAllSousGroupes(idCours)) {
				if(s.getEtudiants().contains(e)) {
					return null;
				}
				
			}
		}
		 Cour c = this.coursRepository.findById(idCours).orElseThrow();
		  sg.setCour(c);

		return sousGroupeRepository.save(sg);
	}

	@Override
	public List<SousGroupe> getAllSousGroupes(Integer idCours) {
		// TODO Auto-generated method stub
		return this.sousGroupeRepository.findAll().stream()
		        .filter(sg -> sg.getCour().getIdCours().equals(idCours))
		        .collect(Collectors.toList());
	}

	@Override
	public SousGroupe modifierSousGroupe(Integer idSousGroupe, SousGroupeDTO sousGroupeDTO) {
		SousGroupe sg=this.sousGroupeRepository.findById(idSousGroupe).orElseThrow();
		
		sg.setNom(sousGroupeDTO.getNom());
		List<Etudiant> list=new ArrayList<>();
		for(String e:sousGroupeDTO.getEtudiants()) {
			Etudiant et=this.etudiantRepository.findByEmail(e).orElseThrow();
			list.add(et);
		}
		sg.setEtudiants(list);
		return sg;
	}

	@Override
	public void deleteSousGroupe(Integer id) {
		SousGroupe sg=this.sousGroupeRepository.findById(id).orElseThrow();
		this.sousGroupeRepository.delete(sg);
	}

	@Override
	public SousGroupe addEtudiantSousGroupe(Integer id, String email) {
	    AtomicBoolean found = new AtomicBoolean(false); // Initialize to false
	    for(SousGroupe s: this.getAllSousGroupes(this.sousGroupeRepository.findById(id).get().getCour().getIdCours())) {
			if(s.getEtudiants().contains(this.etudiantRepository.findByEmail(email))) {
				return null;
			}}
	    SousGroupe sg = this.sousGroupeRepository.findById(id).orElseThrow();
	    Cour c = this.coursRepository.findById(sg.getCour().getIdCours()).orElseThrow();
	    
	    Etudiant studentToAdd = c.getStudents().stream()
	            .filter(e -> e.getEmail().equals(email))
	            .findFirst()
	            .orElse(null);
	   
	    if (studentToAdd != null) {
	       
	        sg.getEtudiants().add(studentToAdd);
	      
	        return this.sousGroupeRepository.save(sg);
	    } else {
	        return null; 
	    }
	    
	    
	}

	
}
