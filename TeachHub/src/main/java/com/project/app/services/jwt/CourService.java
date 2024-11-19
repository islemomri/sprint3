
package com.project.app.services.jwt;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.project.app.dto.CourDTO;
import com.project.app.models.Cour;
import com.project.app.models.Document;
import com.project.app.models.Enseignant;
import com.project.app.models.Etudiant;
import com.project.app.repository.CourRepository;
import com.project.app.repository.DocumentRepository;
import com.project.app.repository.EnseignantRepository;
import com.project.app.repository.EtudiantRepository;







@Service
public class CourService implements IcourService {
	@Autowired
	//c'est une instance de repository
	CourRepository courrep ;

	EnseignantRepository enseignantRepository;
	@Autowired
    EtudiantRepository etudiantRepository;
	@Autowired
    private DocumentRepository documentRepository;

    private final String uploadDir = "uploads/";

    public Document uploadDocument(MultipartFile file, Integer courId, Long enseignantId) throws Exception {
        Cour cour = courrep.findById(courId).orElseThrow();
        Enseignant enseignant = enseignantRepository.findById(enseignantId).orElseThrow();

        Path copyLocation = Paths.get(uploadDir + file.getOriginalFilename());
        Files.copy(file.getInputStream(), copyLocation, StandardCopyOption.REPLACE_EXISTING);

        Document document = new Document(file.getOriginalFilename(), file.getContentType(), copyLocation.toString(), cour, enseignant);
        return documentRepository.save(document);
    }
	
	public CourService(CourRepository courrep, EnseignantRepository enseignantRepository) {
		super();
		this.courrep = courrep;
		this.enseignantRepository = enseignantRepository;
	}
	
	public boolean addStudentToCourseByCode(Long studentId, String courseCode) {
        Cour course = courrep.findByCode(courseCode);
        if (course != null) {
            Etudiant student = etudiantRepository.findById(studentId).orElse(null);
            if (student != null) {
                course.getStudents().add(student);
                courrep.save(course);
                return true;
            }
        }
        return false;
    }
	
	 public boolean addStudentToCourseByEmail(String studentEmail, String courseCode) {
	        Cour course = courrep.findByCode(courseCode);
	        if (course != null) {
	            Etudiant student = etudiantRepository.findByEmail(studentEmail).orElse(null);
	            if (student != null) {
	                course.getStudents().add(student);
	                courrep.save(course);
	                return true;
	            }
	        }
	        return false;
	    }

	 public boolean inviteTeacherByEmail(String teacherEmail, String courseCode) {
		    Cour course = courrep.findByCode(courseCode);
		    if (course != null) {
		        Enseignant teacher = enseignantRepository.findByEmail(teacherEmail).orElse(null);
		        if (teacher != null) {
		            course.getInvitedTeachers().add(teacher);
		            courrep.save(course);
		            return true;
		        }
		    }
		    return false;
		}
	 
	 
	 
	 
	@Override
	public Cour addCour(CourDTO CourDTO,String usernameEns) {
		Cour c=this.mapToEntity(CourDTO);
		Enseignant e=this.enseignantRepository.findByEmail(usernameEns).orElseThrow();
		c.setEnseignant(e);
		c.setCode(UUID.randomUUID().toString().substring(0, 6));
		 Cour savedCour = courrep.save(c);
		 
		return savedCour;
	}

	
	public Cour mapToEntity(CourDTO courDTO) {
		Cour cour=new Cour();
		cour.setIdCours(courDTO.getIdCours());
		cour.setNom(courDTO.getNom());
		cour.setCoefficient(courDTO.getCoefficient());
		cour.setCredits(courDTO.getCredits());
		cour.setMethodeCalcul(courDTO.getMethodeCalcul());
		return cour;
	}

	@Override
	public void deleteCour(int courId) {
		// TODO Auto-generated method stub
		Cour cour=this.courrep.findById(courId).orElseThrow();
		this.courrep.delete(cour);
		
	}

	@Override
	public Cour updateCour(int courId, CourDTO courDTO) {
		// TODO Auto-generated method stub
		Cour cour=this.courrep.findById(courId).orElseThrow();
		cour.setNom(courDTO.getNom());
		cour.setCoefficient(courDTO.getCoefficient());
		cour.setCredits(courDTO.getCredits());
		cour.setMethodeCalcul(courDTO.getMethodeCalcul());
		return this.courrep.save(cour);
	}
	
	@Override
	public List<Cour> getCoursByEtudiantId(Long etudiantId) {
	    return courrep.findByStudents_Id(etudiantId); 
	}
	
	@Override
	public List<Cour> getAllCours(Long id) {
		List<Cour> c= courrep.findByEnseignant_Id(id);
		
			return c;	
				
	}
	
	@Override public List<Document> getDocumentsByCourId(Integer courId) { 
		Cour cour = courrep.findById(courId).orElseThrow();
		return documentRepository.findByCour(cour);
		}
	
	public Document getDocumentById(Long documentId) { 
		return documentRepository.findById(documentId)
				.orElseThrow(() -> new RuntimeException("Document non trouvé"));
		}
	
	public Cour getCoursById(Integer courId) { 
		return courrep.findById(courId)
				.orElseThrow(() -> new RuntimeException("Cours non trouvé"));
		}
	
	public List<Cour> getCoursesForInvitedTeacher(String teacherEmail) {
	    Enseignant teacher = enseignantRepository.findByEmail(teacherEmail).orElse(null);
	    if (teacher != null) {
	        return courrep.findByInvitedTeachersContains(teacher);
	    }
	    return new ArrayList<>();
	}

}
