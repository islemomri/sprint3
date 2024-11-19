package com.project.app.controllers;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.project.app.dto.CourDTO;
import com.project.app.dto.DevoirDTO;
import com.project.app.dto.SousGroupeDTO;
import com.project.app.models.Cour;
import com.project.app.models.Devoir;
import com.project.app.models.SousGroupe;
import com.project.app.services.jwt.SousGroupeService;

@RestController
public class SousGroupeController {

	@Autowired
	private SousGroupeService sousGroupeService;
	
	@PostMapping(value="/addsousgroupe/{id}")
	public SousGroupe addSousGroupe(@RequestBody SousGroupeDTO sousGroupeDTO,@PathVariable("id") int idCours ) {
		SousGroupe sg=sousGroupeService.addSousGroupe(sousGroupeDTO, idCours);
		if(sg!=null) {
		return sousGroupeService.addSousGroupe(sousGroupeDTO, idCours);}
		else {
			throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Student does already exist in another sous groupe");
		}
	}
	@GetMapping(value = "/sousgroupe/{idCours}")
    public List<SousGroupe> getAllDevoirs(@PathVariable Integer idCours) {
        return this.sousGroupeService.getAllSousGroupes(idCours); 
    }
	@PutMapping("/updatesousgroupe/{idSousGroupe}")
    public ResponseEntity<SousGroupe> modifierDevoir(@PathVariable Integer idSousGroupe, @RequestBody SousGroupeDTO SousGroupeDTO) {
		SousGroupe updatedSousGroupe = this.sousGroupeService.modifierSousGroupe(idSousGroupe, SousGroupeDTO);
        if (updatedSousGroupe != null) {
            return new ResponseEntity<>(updatedSousGroupe, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); 
        }
    }
	
	@DeleteMapping("/deletesousgroupe/{id}")
	 public ResponseEntity<String> deleteSousgroupe(@PathVariable(value="id") Integer id){
		 this.sousGroupeService.deleteSousGroupe(id);
		 return ResponseEntity.status(HttpStatus.OK).body("sous groupe deleted successfully");
	 }
	
	@PostMapping("/addEtudiantSousgroupe/{idSousGroupe}/{email}")
	public SousGroupe addEtudiantSousGroupe(@PathVariable Integer idSousGroupe, @PathVariable String email) {
	    SousGroupe add = this.sousGroupeService.addEtudiantSousGroupe(idSousGroupe, email);
	    if (add!=null) {
	        return add;
	    } else {
	    	throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Student does not exist or could not be added");
	    	  
	    }
		
	}

}
