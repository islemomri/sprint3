package com.project.app.services.jwt;

import java.util.List;

import com.project.app.dto.DevoirRenduDTO;
import com.project.app.models.DevoirRendu;

public interface IDevoirRenduService {
	
	public DevoirRendu addDevoirRendu(DevoirRenduDTO DevoirRenduDTO,Long idDevoir,String emailEtudiant);
	public List<DevoirRendu> getAllDevoirsRendu(Long idDevoir);
	public DevoirRendu modifierDevoirRendu(Long idDevoirRendu, String email, DevoirRendu devoirRendu);
	 public void deleteDevoirRendu(Long idDevoir,String email);
	 public boolean checkDevoirRendu(Long idDevoir,String email);
}
