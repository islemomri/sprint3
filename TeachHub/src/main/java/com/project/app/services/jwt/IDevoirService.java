package com.project.app.services.jwt;

import java.util.List;

import com.project.app.dto.DevoirDTO;
import com.project.app.models.Devoir;

public interface IDevoirService {
	public Devoir addDevoir(DevoirDTO DevoirDTO,int idCours);
	public List<Devoir> getAllDevoirs(Integer idCours);
	 public Devoir modifierDevoir(Long idDevoir, DevoirDTO devoirDTO);
	 public void deleteDevoir(Long id);
	 public List<Devoir> getDevoirsByEtudiantId(String email, Integer idCours);
}
