package com.project.app.controllers;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

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


import com.project.app.dto.DevoirRenduDTO;
import com.project.app.dto.EvaluationDTO;
import com.project.app.models.DevoirRendu;
import com.project.app.services.jwt.DevoirRenduService;


import jakarta.transaction.Transactional;

@RestController
public class DevoirRenduController {

	@Autowired
    private DevoirRenduService devoirRenduService;
	
	
	@Transactional
	@PostMapping(value = "/addDevoirRendu/{idDevoir}/{email}")
	public ResponseEntity<DevoirRendu> addDevoirRendu(
	        @RequestParam(value = "pdfs", required = false) MultipartFile[] pdfs, // Liste de fichiers
	        @PathVariable Long idDevoir,
	        @PathVariable String email) {

	    
	    DevoirRenduDTO devoirRenduDTO = new DevoirRenduDTO();

	   
	    if (pdfs != null && pdfs.length > 0) {
	        List<byte[]> pdfList = new ArrayList<>();

	        
	        for (MultipartFile pdf : pdfs) {
	            if (!pdf.isEmpty()) {
	                try {
	                	 System.out.println("Nom du fichier: " + pdf.getOriginalFilename());
	                     System.out.println("Taille du fichier: " + pdf.getSize() + " octets");
	                     System.out.println("Type de fichier: " + pdf.getContentType());

	                    pdfList.add(pdf.getBytes());
	                } catch (IOException e) {
	                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	                }
	            }
	        }

	       
	        devoirRenduDTO.setPdfs(pdfList);
	    }

	   
	    DevoirRendu savedDevoir = devoirRenduService.addDevoirRendu(devoirRenduDTO, idDevoir, email);

	   
	    return ResponseEntity.status(HttpStatus.CREATED).body(savedDevoir);
	}

	
	@GetMapping(value = "/DevoirRendu/{idDevoir}")
    public List<DevoirRendu> getAllDevoirsRendu(@PathVariable Long idDevoir) {
        return devoirRenduService.getAllDevoirsRendu(idDevoir); 
    }
	
	
	
	@GetMapping("/devoirRendu/download/{idDevoir}/{email}")
	public ResponseEntity<ByteArrayResource> downloadDevoirPDF(@PathVariable Long idDevoir, @PathVariable String email) {
	    // Utiliser la méthode getDevoirsPDFs qui retourne une liste de PDF
	    List<byte[]> pdfDataList = devoirRenduService.getDevoirsPDFs(idDevoir, email);

	    if (pdfDataList != null && !pdfDataList.isEmpty()) {
	        // Création d'un flux de sortie pour la compression ZIP
	        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
	        try (ZipOutputStream zipOut = new ZipOutputStream(byteArrayOutputStream)) {
	            int i = 1;
	            // Parcours des fichiers PDF et ajout dans le fichier ZIP
	            for (byte[] pdfData : pdfDataList) {
	                // Créer une entrée dans le ZIP pour chaque fichier PDF
	                ZipEntry zipEntry = new ZipEntry("devoirRendu_" + idDevoir + "_" + i + ".pdf");
	                zipOut.putNextEntry(zipEntry);
	                zipOut.write(pdfData);
	                zipOut.closeEntry();
	                i++;
	            }
	        } catch (IOException e) {
	            return ResponseEntity.internalServerError().build();  // En cas d'erreur lors de la création du ZIP
	        }

	        // Création de la ressource pour le téléchargement du fichier ZIP
	        ByteArrayResource resource = new ByteArrayResource(byteArrayOutputStream.toByteArray());

	        // Retourner le fichier ZIP avec un en-tête approprié
	        return ResponseEntity.ok()
	                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=devoirsRendus_" + idDevoir + ".zip")
	                .contentType(MediaType.APPLICATION_OCTET_STREAM)  // Type MIME pour un fichier binaire (ZIP)
	                .body(resource);
	    } else {
	        // Si aucun fichier PDF n'est trouvé, renvoyer une réponse "not found"
	        return ResponseEntity.notFound().build();
	    }
	}

	@PutMapping("/updateDevoirRendu/{idDevoirRendu}/{email}")
	public ResponseEntity<DevoirRendu> modifierDevoirRendu(
	        @PathVariable Long idDevoirRendu,
	        @PathVariable String email,
	        @RequestParam(value = "pdfs", required = false) MultipartFile[] pdfFiles) {

	    // Étape 1: Vérifier si le devoir rendu existe avant de tenter de le supprimer
	    DevoirRendu existingDevoirRendu = devoirRenduService.getDevoirRendu(idDevoirRendu, email);
	    if (existingDevoirRendu == null) {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);  // Retourner 404 si non trouvé
	    }

	    // Étape 2: Vider la liste de fichiers PDF existants
	    existingDevoirRendu.setPdfs(new ArrayList<>());  // Vider la liste des PDF existants

	    // Étape 3: Ajouter les nouveaux fichiers PDF à la liste
	    if (pdfFiles != null && pdfFiles.length > 0) {
	        List<byte[]> pdfList = new ArrayList<>();
	        for (MultipartFile pdf : pdfFiles) {
	            if (!pdf.isEmpty()) {
	                try {
	                    pdfList.add(pdf.getBytes());
	                    System.out.println("Fichier PDF reçu : " + pdf.getOriginalFilename() + ", Taille : " + pdf.getSize() + " octets.");
	                } catch (IOException e) {
	                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	                }
	            }
	        }
	        existingDevoirRendu.setPdfs(pdfList);  // Remplir avec les nouveaux fichiers
	    } else {
	        // Si aucun fichier n'est sélectionné, retourner une erreur
	        System.out.println("Aucun fichier PDF reçu.");
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
	    }

	    // Étape 4: Mettre à jour le devoir rendu dans la base de données
	    try {
	        DevoirRendu updatedDevoir = devoirRenduService.modifierDevoirRendu(idDevoirRendu, email, existingDevoirRendu);
	        return ResponseEntity.status(HttpStatus.OK).body(updatedDevoir);
	    } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	    }
	}


 
	 @DeleteMapping("/deleteDevoirRendu/{id}/{email}")
	 public ResponseEntity<String> deleteDevoir(@PathVariable(value="id") Long id,@PathVariable String email){
		 this.devoirRenduService.deleteDevoirRendu(id,email);
		 return ResponseEntity.status(HttpStatus.OK).body("Devoir rendu deleted successfully");
	 }
	 @GetMapping(value = "/CheckDevoirRendu/{idDevoir}/{email}")
	    public ResponseEntity<String> checkDevoirsRendu(@PathVariable Long idDevoir,@PathVariable String email) {
		 if(this.devoirRenduService.checkDevoirRendu(idDevoir, email)) {
	        return ResponseEntity.status(HttpStatus.OK).body("devoir deja rendu");
	    }else {
	    	return ResponseEntity.status(HttpStatus.NOT_FOUND).body("devoir n'est pas rendu");
	    }}
	 @PostMapping("/evaluerDevoir")
	 public ResponseEntity<DevoirRendu> evaluerDevoir(@RequestBody EvaluationDTO evaluationDTO) {
	     try {
	         System.out.println("Évaluation reçue : " + evaluationDTO); // Journalisation
	         DevoirRendu evaluatedDevoir = devoirRenduService.evaluerDevoir(evaluationDTO);
	         return ResponseEntity.ok(evaluatedDevoir);
	     } catch (Exception e) {
	         e.printStackTrace();
	         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	     }
	 }



	 
	 @GetMapping("/devoirRendu/evaluation/{idDevoir}/{email}")
	 public ResponseEntity<EvaluationDTO> getEvaluation(@PathVariable Long idDevoir, @PathVariable String email) {
	     DevoirRendu devoirRendu = devoirRenduService.findByDevoirIdAndEtudiantEmail(idDevoir, email)
	             .orElseThrow(() -> new IllegalArgumentException("Devoir rendu non trouvé"));

	     EvaluationDTO evaluationDTO = new EvaluationDTO();
	     evaluationDTO.setIdDevoirRendu(devoirRendu.getIdDevoirRendu());
	     evaluationDTO.setNote(devoirRendu.getNote());
	     evaluationDTO.setCommentaire(devoirRendu.getCommentaire());

	     return ResponseEntity.ok(evaluationDTO);
	 }
	 @GetMapping("/devoirRendu/{idDevoirRendu}")
	 public ResponseEntity<DevoirRenduDTO> getDevoirRenduById(@PathVariable Long idDevoirRendu) {
	     DevoirRendu devoirRendu = devoirRenduService.getDevoirRenduById(idDevoirRendu);
	     if (devoirRendu != null) {
	         // Convertir le devoir rendu en DTO pour le renvoyer
	         DevoirRenduDTO devoirRenduDTO = new DevoirRenduDTO();
	         devoirRenduDTO.setIdDevoirRendu(devoirRendu.getIdDevoirRendu());
	         devoirRenduDTO.setNote(devoirRendu.getNote());
	         devoirRenduDTO.setCommentaire(devoirRendu.getCommentaire());
	         // Ajoutez d'autres propriétés du devoir rendu selon les besoins
	         return ResponseEntity.ok(devoirRenduDTO);
	     } else {
	         return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
	     }
	 }

	 
	 @GetMapping("/devoirRendu/{idDevoir}/{email}")
	 public ResponseEntity<DevoirRenduDTO> getDevoirRendu(@PathVariable Long idDevoir, @PathVariable String email) {
	     DevoirRendu devoirRendu = devoirRenduService.findByDevoirIdAndEtudiantEmail(idDevoir, email)
	             .orElseThrow(() -> new IllegalArgumentException("Devoir rendu non trouvé"));

	     DevoirRenduDTO devoirRenduDTO = new DevoirRenduDTO();
	     devoirRenduDTO.setIdDevoirRendu(devoirRendu.getIdDevoirRendu());
	     devoirRenduDTO.setNote(devoirRendu.getNote());
	     devoirRenduDTO.setCommentaire(devoirRendu.getCommentaire());
	     // Ajoutez les autres champs nécessaires

	     return ResponseEntity.ok(devoirRenduDTO);
	 }
}