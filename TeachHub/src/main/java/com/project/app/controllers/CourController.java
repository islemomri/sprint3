package com.project.app.controllers;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.core.io.Resource; 
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

import com.project.app.dto.CourDTO;
import com.project.app.models.Cour;
import com.project.app.models.Document;
import com.project.app.models.Enseignant;
import com.project.app.repository.CourRepository;
import com.project.app.repository.EnseignantRepository;
import com.project.app.services.jwt.IcourService;





@RestController
public class CourController {
	@Autowired
	private IcourService  courserv;
	@Autowired
	private CourRepository courrep;
	@Autowired
	private EnseignantRepository enseignantRepository;
	

	
	@PostMapping(value="/addcour/{usernameEns}")
	public Cour addUser(@RequestBody CourDTO courDTO,@PathVariable("usernameEns") String usernameEns ) {
		return courserv.addCour(courDTO,usernameEns);
	}
	
	@GetMapping("/cours/{id}") 
	public ResponseEntity<Cour> getCoursById(@PathVariable Integer id) { 
		Cour cour = courserv.getCoursById(id);
		return ResponseEntity.ok(cour); 
		}
	
	@PutMapping("/updatecours/{id}")
	public ResponseEntity<Cour> updateCour(@PathVariable(value="id") int id,@RequestBody CourDTO courDTO){
		Cour cour=this.courserv.updateCour( id,courDTO);
		return ResponseEntity.status(HttpStatus.OK).body(cour);
		
	}
	@DeleteMapping("/deletecours/{id}")
	 public ResponseEntity<String> deleteCour(@PathVariable(value="id") int id){
		 this.courserv.deleteCour(id);
		 return ResponseEntity.status(HttpStatus.OK).body("Cours deleted successfully");
	 }
	
	@PostMapping("/{courseCode}/inviteById/{studentId}")
    public ResponseEntity<String> inviteStudentById(
            @PathVariable String courseCode, @PathVariable Long studentId) {
        boolean result = courserv.addStudentToCourseByCode(studentId, courseCode);
        if (result) {
            return ResponseEntity.ok("L'étudiant a été invité avec succès.");
        }
        return ResponseEntity.badRequest().body("Impossible d'inviter l'étudiant.");
    }

	@PostMapping("/{courseCode}/inviteByEmail")
	public ResponseEntity<String> inviteStudentByEmail(
	        @PathVariable String courseCode, @RequestBody Map<String, String> request) {
	    String studentEmail = request.get("studentEmail");
	    boolean result = courserv.addStudentToCourseByEmail(studentEmail, courseCode);
	    if (result) {
	        return ResponseEntity.ok("L'étudiant a été invité avec succès.");
	    }
	    return ResponseEntity.badRequest().body("Impossible d'inviter l'étudiant.");
	}
	
	@PostMapping("/{courseCode}/inviteTeacherByEmail")
	public ResponseEntity<String> inviteTeacherByEmail(
	        @PathVariable String courseCode, @RequestBody Map<String, String> request) {
	    String teacherEmail = request.get("teacherEmail");
	    boolean result = courserv.inviteTeacherByEmail(teacherEmail, courseCode);
	    if (result) {
	        return ResponseEntity.ok("L'enseignant a été invité avec succès.");
	    }else {
	    return ResponseEntity.badRequest().body("Impossible d'inviter l'enseignant.");
	}}
	
	
	@GetMapping("/cours/enseignant/{id}")
	public ResponseEntity<?> getCoursByEnseignant(@PathVariable(value = "id") Long id) {
	    // Vérification de l'ID
	    if (id == null || id <= 0) {
	        return ResponseEntity.badRequest().body("L'ID de l'enseignant est invalide.");
	    }

	    try {
	        // Appel au service pour récupérer les cours
	        List<Cour> cours = courserv.getAllCours(id);
	        
	        if (cours == null || cours.isEmpty()) {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND)
	                    .body("Aucun cours trouvé pour l'enseignant avec ID : " + id);
	        }

	        // Retour des cours en réponse
	        return ResponseEntity.ok(cours);
	    } catch (Exception e) {
	        // Erreur interne du serveur
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                .body("Erreur interne du serveur : " + e.getMessage());
	    }
	}


	@GetMapping("/cours/invited-enseignant/{email}")
	public ResponseEntity<List<Cour>> getCoursesForInvitedTeacher(@PathVariable(value = "email") String email) {
	    List<Cour> cours = courserv.getCoursesForInvitedTeacher(email);
	    return ResponseEntity.ok(cours);
	}





    @GetMapping("/cours/etudiant/{id}")
    public ResponseEntity<List<Cour>> getCoursByEtudiant(@PathVariable(value = "id") Long id) {
        List<Cour> cours = courserv.getCoursByEtudiantId(id);
        return ResponseEntity.ok(cours);
    }
    
    @PostMapping("/upload/{courId}/{enseignantId}")
    public ResponseEntity<String> uploadDocument(
            @RequestParam("file") MultipartFile file,
            @PathVariable Integer courId,
            @PathVariable Long enseignantId) {

        try {
            Document document = courserv.uploadDocument(file, courId, enseignantId);
            return ResponseEntity.status(HttpStatus.CREATED).body("Document uploaded successfully: " + document.getName());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload document");
        }
    }
    
    @GetMapping("/documents/{documentId}")
    public ResponseEntity<Resource> downloadDocument(@PathVariable Long documentId) {
        Document document = courserv.getDocumentById(documentId); 
        Path path = Paths.get(document.getPath()); 
        Resource resource;
        try { 
            resource = new UrlResource(path.toUri());
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la récupération du document", e);
        }
        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + document.getName() + "\"")
            .header(HttpHeaders.CONTENT_TYPE, "application/pdf") // Spécifiez le type de contenu
            .body(resource);
    }
    
    @GetMapping("/cours/{courId}/documents")
	public ResponseEntity<List<Document>> getDocumentsByCourId(@PathVariable Integer courId) { 
		List<Document> documents = courserv.getDocumentsByCourId(courId); 
		if (documents == null || documents.isEmpty())
		{ System.out.println("Aucun document trouvé pour le cours ID " + courId); }
		else { 
			System.out.println("Documents trouvés pour le cours ID " + courId + ": " + documents.size());
			} 
		return ResponseEntity.ok(documents);
		}

}
