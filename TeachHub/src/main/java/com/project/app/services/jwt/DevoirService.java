package com.project.app.services.jwt;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.project.app.dto.CourDTO;
import com.project.app.dto.DevoirDTO;
import com.project.app.models.Cour;
import com.project.app.models.Devoir;
import com.project.app.models.SousGroupe;
import com.project.app.repository.CourRepository;
import com.project.app.repository.DevoirRepository;
import com.project.app.repository.EtudiantRepository;
import com.project.app.repository.SousGroupeRepository;

import jakarta.transaction.Transactional;

@Service
public class DevoirService implements IDevoirService {
	@Autowired
    private DevoirRepository devoirRepository;
	@Autowired
    private CourRepository coursRepository;
	@Autowired
    private SousGroupeRepository sousGroupeRepository;
	@Autowired
	private PlatformTransactionManager transactionManager;
	@Autowired
    private EtudiantRepository etudiantRepository;
	
	@Transactional
	@Override 
	public Devoir addDevoir(DevoirDTO devoirDTO, int idCours) {
	    Devoir dev = this.mapToEntity(devoirDTO); // Convert DTO to Entity
	    Cour c = this.coursRepository.findById(idCours).orElseThrow();
	    dev.setCours(c);

	    // Set PDF file if it exists
	    if (devoirDTO.getPdf() != null && devoirDTO.getPdf().length != 0) {
	        dev.setPdf(devoirDTO.getPdf());  // Setting the PDF in the entity 'dev'
	    }

	    return devoirRepository.save(dev);  // Save the entity with the PDF to the database
	}
	

	@Override
	public List<Devoir> getAllDevoirs(Integer idCours) {
		  return this.devoirRepository.findAll().stream()
			        .filter(devoir -> devoir.getCours().getIdCours().equals(idCours))
			         .collect(Collectors.toList());
	}

	@Transactional
	public byte[] getDevoirPDF(Long idDevoir) {
	    TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());
	    try {
	        Devoir devoir = this.devoirRepository.findById(idDevoir).orElseThrow();
	        if (devoir != null && devoir.getPdf() != null) {
	            return devoir.getPdf(); // retourner le contenu du PDF en tant que tableau de bytes
	        } else {
	            return null; // pas de fichier PDF trouvé
	        }
	    } finally {
	        transactionManager.commit(status);
	    }
	}
	 
	@Override
	public Devoir modifierDevoir(Long idDevoir, DevoirDTO devoirDTO) {
		 Devoir dev=this.devoirRepository.findById(idDevoir).orElseThrow();
		 dev.setBareme(devoirDTO.getBareme());
		 dev.setDateLimite(devoirDTO.getDateLimite());
		 dev.setDescription(devoirDTO.getDescription());
		 dev.setPonderation(devoirDTO.getPonderation());
		 dev.setStatut(devoirDTO.getStatut());
		 dev.setTypedevoir(devoirDTO.getTypedevoir());
		 dev.setMaxDocuments(devoirDTO.getMaxDocuments());
		 JSONArray jsonArray = new JSONArray(devoirDTO.getSousGroupes());
	      List<Integer> sousGroupes = new ArrayList<>();
	      for (int i = 0; i < jsonArray.length(); i++) {
	        sousGroupes.add(jsonArray.getInt(i));
	      }
		 List<SousGroupe> list=new ArrayList<>();
		 
			for(Integer id:sousGroupes) {
				list.add(this.sousGroupeRepository.findById(id).orElseThrow());
			}
			dev.setSousGroupes(list);
		 return this.devoirRepository.save(dev);
	}

	@Override
	public void deleteDevoir(Long id) {
		// TODO Auto-generated method stub
		Devoir devoir=this.devoirRepository.findById(id).orElseThrow();
		this.devoirRepository.delete(devoir);	
	}
	

	public Devoir mapToEntity(DevoirDTO DevoirDTO) {
		Devoir dev=new Devoir();
		dev.setIdDevoir(DevoirDTO.getIdDevoir());
		dev.setBareme(DevoirDTO.getBareme());
		dev.setDateLimite(DevoirDTO.getDateLimite());
		dev.setPonderation(DevoirDTO.getPonderation());
		dev.setDescription(DevoirDTO.getDescription());
		dev.setStatut(DevoirDTO.getStatut());
		dev.setTypedevoir(DevoirDTO.getTypedevoir());
		dev.setMaxDocuments(DevoirDTO.getMaxDocuments());
		List<SousGroupe> list=new ArrayList<>();
		JSONArray jsonArray = new JSONArray(DevoirDTO.getSousGroupes());
	      List<Integer> sousGroupes = new ArrayList<>();
	      for (int i = 0; i < jsonArray.length(); i++) {
	        sousGroupes.add(jsonArray.getInt(i));
	      }
		for(Integer id:sousGroupes) {
			list.add(this.sousGroupeRepository.findById(id).orElseThrow());
		}
		dev.setSousGroupes(list);
		return dev;
	}
	
	
	@Override
	public List<Devoir> getDevoirsByEtudiantId(String email, Integer idCours) {
		// TODO Auto-generated method stub
		List<Devoir> list=this.getAllDevoirs(idCours);
//		List<Cour> c=this.courService.getAllCours(this.etudiantRepository.findByEmail(email).get().getId());
		List<Devoir> dev=new ArrayList<>();
		for(Devoir d:list) {
			if(d.getSousGroupes().isEmpty()) {
				dev.add(d);
			}else {
			for(SousGroupe sg:d.getSousGroupes()) {
				if(sg.getEtudiants().contains(this.etudiantRepository.findByEmail(email).orElseThrow())) {
					dev.add(d);
				}
			}
		}}
		return dev;
	}



}
