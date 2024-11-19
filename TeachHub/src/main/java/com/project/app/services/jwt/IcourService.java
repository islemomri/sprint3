package com.project.app.services.jwt;

import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.project.app.dto.CourDTO;
import com.project.app.models.Cour;
import com.project.app.models.Document;
import com.project.app.models.Enseignant;



public interface IcourService {
	public Cour addCour(CourDTO Cour,String usernameEns);
	public List<Cour> getAllCours(Long id); 
	void deleteCour(int courId);
	Cour updateCour(int courId,CourDTO courDTO);
	public boolean addStudentToCourseByCode(Long studentId, String courseCode);
	public boolean addStudentToCourseByEmail(String studentEmail, String courseCode);
	public List<Cour> getCoursByEtudiantId(Long etudiantId);
	public boolean inviteTeacherByEmail(String teacherEmail, String courseCode);
	public Document uploadDocument(MultipartFile file, Integer courId, Long enseignantId)throws Exception;
	List<Document> getDocumentsByCourId(Integer courId);
	public Document getDocumentById(Long documentId);
	public Cour getCoursById(Integer id);
	public List<Cour> getCoursesForInvitedTeacher(String teacherEmail);
	 
}
