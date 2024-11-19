package com.project.app.controllers;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.project.app.dto.DevoirDTO;
import com.project.app.models.Cour;
import com.project.app.models.Devoir;
import com.project.app.services.jwt.DevoirService;

import jakarta.transaction.Transactional;

@RestController
public class DevoirController {
	 @Autowired
	    private DevoirService devoirService;
	 
	 @Transactional
	 @PostMapping(value = "/addDevoir/{idCours}")
	  public ResponseEntity<Devoir> addDevoir(
	      @RequestParam("typedevoir") String typedevoir,
	      @RequestParam("description") String description,
	      @RequestParam("ponderation") float ponderation,
	      @RequestParam("bareme") String bareme,
	      @RequestParam("dateLimite") @DateTimeFormat(pattern = "yyyy-MM-dd") Date dateLimite,
	      @RequestParam("statut") String statut,
	      @RequestParam(value = "pdf", required = false) MultipartFile pdf,
	      @RequestParam("sousGroupes") String sousGroupesJson, // Read as JSON string
	      @RequestParam("maxDocuments") Integer maxdocuments,
	      @PathVariable int idCours) {
		 System.out.println("maxdocuments received: " + maxdocuments);

	    try {
	      // Parse the JSON string into a List<Integer> without ObjectMapper
	      

	      DevoirDTO devoirDTO = new DevoirDTO();
	      devoirDTO.setTypedevoir(typedevoir);
	      devoirDTO.setDescription(description);
	      devoirDTO.setPonderation(ponderation);
	      devoirDTO.setBareme(bareme);
	      devoirDTO.setDateLimite(dateLimite);
	      devoirDTO.setStatut(statut);
	      devoirDTO.setSousGroupes(sousGroupesJson);
	      devoirDTO.setMaxDocuments(maxdocuments);

	      // Handle the PDF file if it exists
	      if (pdf != null && !pdf.isEmpty()) {
	        devoirDTO.setPdf(pdf.getBytes());
	      }

	      // Save the devoir using the service
	      Devoir savedDevoir = devoirService.addDevoir(devoirDTO, idCours);
	      return ResponseEntity.status(HttpStatus.CREATED).body(savedDevoir);

	    } catch (IOException e) {
	      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	    }
	  }

	   
	 @GetMapping(value = "/Devoirs/{idCours}")
	 public List<Devoir> getAllDevoirs(@PathVariable Integer idCours) {
	     List<Devoir> devoirs = devoirService.getAllDevoirs(idCours);

	     // Appliquer une valeur par défaut pour maxDocuments si null
	     for (Devoir devoir : devoirs) {
	         if (devoir.getMaxDocuments() == null) {
	             devoir.setMaxDocuments(5);  // Par exemple, une valeur par défaut de 5
	         }
	     }

	     return devoirs;
	 }

	 @GetMapping("/devoir/download/{idDevoir}")
	    public ResponseEntity<ByteArrayResource> downloadDevoirPDF(@PathVariable Long idDevoir) {
	        byte[] pdfData = devoirService.getDevoirPDF(idDevoir);
	        if (pdfData != null) {
	            ByteArrayResource resource = new ByteArrayResource(pdfData);
	            return ResponseEntity.ok()
	                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=devoir_" + idDevoir + ".pdf")
	                    .contentType(MediaType.APPLICATION_PDF)
	                    .body(resource);
	        } else {
	            return ResponseEntity.notFound().build();
	        }
	    }
	 @PutMapping("/updateDevoir/{idDevoir}")
	    public ResponseEntity<Devoir> modifierDevoir(@PathVariable Long idDevoir, @RequestBody DevoirDTO devoirDTO) {
	        Devoir updatedDevoir = devoirService.modifierDevoir(idDevoir, devoirDTO);
	        if (updatedDevoir != null) {
	            return new ResponseEntity<>(updatedDevoir, HttpStatus.OK);
	        } else {
	            return new ResponseEntity<>(HttpStatus.NOT_FOUND); 
	        }
	    }
	 @DeleteMapping("/deleteDevoir/{id}")
	 public ResponseEntity<String> deleteDevoir(@PathVariable(value="id") Long id){
		 this.devoirService.deleteDevoir(id);
		 return ResponseEntity.status(HttpStatus.OK).body("Devoir deleted successfully");
	 }
	 
	 @GetMapping("/devoir/etudiant/{email}/{idCours}")
	    public ResponseEntity<List<Devoir>> getCoursByEtudiant(@PathVariable(value = "email") String email,@PathVariable(value = "idCours") Integer idCours) {
		 List<Devoir> devoirs = this.devoirService.getDevoirsByEtudiantId(email,idCours);
	        return ResponseEntity.ok(devoirs);
	    }
	 
}
