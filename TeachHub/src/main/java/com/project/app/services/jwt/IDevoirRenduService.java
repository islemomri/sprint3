package com.project.app.services.jwt;

import java.util.List;
import java.util.Optional;

import com.project.app.dto.DevoirRenduDTO;
import com.project.app.dto.EvaluationDTO;
import com.project.app.models.DevoirRendu;

public interface IDevoirRenduService {
	
	public DevoirRendu addDevoirRendu(DevoirRenduDTO DevoirRenduDTO,Long idDevoir,String emailEtudiant);
	public List<DevoirRendu> getAllDevoirsRendu(Long idDevoir);
	public DevoirRendu modifierDevoirRendu(Long idDevoirRendu, String email, DevoirRendu devoirRendu);
	 public void deleteDevoirRendu(Long idDevoir,String email);
	
	 public Optional<DevoirRendu> findByDevoirIdAndEtudiantEmail(Long idDevoir, String email);
	 public DevoirRendu evaluerDevoir(EvaluationDTO evaluationDTO);
}
