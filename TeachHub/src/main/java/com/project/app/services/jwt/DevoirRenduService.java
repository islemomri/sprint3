package com.project.app.services.jwt;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;


import com.project.app.dto.DevoirRenduDTO;
import com.project.app.dto.DevoirRenduResponse;
import com.project.app.dto.EvaluationDTO;
import com.project.app.models.Devoir;
import com.project.app.models.DevoirRendu;
import com.project.app.models.Etudiant;
import com.project.app.repository.DevoirRenduRepository;
import com.project.app.repository.DevoirRepository;
import com.project.app.repository.EtudiantRepository;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import jakarta.transaction.Transactional;

@Service
public class DevoirRenduService implements IDevoirRenduService{
	@Autowired
    private DevoirRepository devoirRepository;
	@Autowired
    private DevoirRenduRepository devoirRenduRepository;
	@Autowired
    private EtudiantRepository etudiantRepository;
	@Autowired
	private PlatformTransactionManager transactionManager;
	
	
	
	public DevoirRendu mapToEntity(DevoirRenduDTO DevoirDTO) {
		DevoirRendu dev=new DevoirRendu();
		dev.setIdDevoirRendu(DevoirDTO.getIdDevoirRendu());
		dev.setPdfs(DevoirDTO.getPdfs());
		return dev;
	}

	
	@Transactional
	@Override
	public DevoirRendu addDevoirRendu(DevoirRenduDTO devoirRenduDTO, Long idDevoir, String emailEtudiant) {
	   
	    
	    
	    Devoir dev = this.devoirRepository.findById(idDevoir).orElseThrow();
	    Etudiant et = this.etudiantRepository.findByEmail(emailEtudiant).orElseThrow();
	    if (devoirRenduDTO.getPdfs() != null && devoirRenduDTO.getPdfs().size() > dev.getMaxDocuments()) {
	        throw new IllegalArgumentException("Le nombre de fichiers PDF dépasse la limite autorisée de " + dev.getMaxDocuments());
	    }
	    DevoirRendu devR = this.mapToEntity(devoirRenduDTO);

	   
	    devR.setDevoir(dev);
	    devR.setEtudiant(et);

	    
	    

	    
	    if (devoirRenduDTO.getPdfs() != null && !devoirRenduDTO.getPdfs().isEmpty()) {
	        devR.setPdfs(devoirRenduDTO.getPdfs());  
	    }

	    
	    return this.devoirRenduRepository.save(devR);
	}


	public boolean checkDevoirRendu(Long idDevoir, String email) {
	    return this.getAllDevoirsRendu(idDevoir).stream()
	               .anyMatch(devoirR -> devoirR.getEtudiant().getEmail().equals(email));
	}

	@Override
	public List<DevoirRendu> getAllDevoirsRendu(Long idDevoir) {
		// TODO Auto-generated method stub
		List<DevoirRendu> l=new ArrayList<>();
		 this.devoirRenduRepository.findAll().stream()
		        .collect(Collectors.toList()).forEach(d->{
		        if(d.getDevoir().getIdDevoir()==idDevoir) {
		        	l.add(d);
		        }
		        });
		 return l;
	}
	public List<DevoirRenduResponse> getAllDevoirsRenduEnseignant(Long idDevoir) {
		// TODO Auto-generated method stub
		List<DevoirRenduResponse> l=new ArrayList<>();
		 this.devoirRenduRepository.findAll().stream()
		        .collect(Collectors.toList()).forEach(d->{
		        if(d.getDevoir().getIdDevoir()==idDevoir) {
		        	DevoirRenduResponse res=new DevoirRenduResponse();
		        	res.setIdDevoirRendu(d.getIdDevoirRendu());
		        	res.setEmail(d.getEtudiant().getEmail());
		        	res.setPdfs(d.getPdfs());
		        	l.add(res);
		        }
		        });
		 return l;
	}

	
	@Transactional
	@Override
	public DevoirRendu modifierDevoirRendu(Long idDevoirRendu, String email, DevoirRendu devoirRendu) {
	    // Recherche du devoir rendu existant
	    DevoirRendu existingDevoirRendu = devoirRenduRepository.findById(idDevoirRendu)
	            .orElseThrow(() -> new IllegalArgumentException("DevoirRendu non trouvé"));

	    // Vérification si l'étudiant correspond à l'email
	    if (!existingDevoirRendu.getEtudiant().getEmail().equals(email)) {
	        throw new IllegalArgumentException("Cet étudiant n'a pas rendu ce devoir");
	    }

	    // Mise à jour des fichiers PDF
	    if (devoirRendu.getPdfs() != null && !devoirRendu.getPdfs().isEmpty()) {
	        existingDevoirRendu.setPdfs(devoirRendu.getPdfs());  // Remplir avec les nouveaux fichiers PDF
	    }

	    // Sauvegarde de l'entité mise à jour
	    return devoirRenduRepository.save(existingDevoirRendu);
	}

	@Transactional
	public DevoirRendu getDevoirRendu(Long idDevoirRendu, String email) {
	    
	    DevoirRendu devoirRendu = devoirRenduRepository.findById(idDevoirRendu)
	            .orElseThrow(() -> new IllegalArgumentException("DevoirRendu non trouvé"));

	    
	    if (!devoirRendu.getEtudiant().getEmail().equals(email)) {
	        throw new IllegalArgumentException("Cet étudiant n'a pas rendu ce devoir");
	    }

	   
	    return devoirRendu;
	}
	public DevoirRendu getDevoirRenduById(Long idDevoirRendu) {
	    return devoirRenduRepository.findById(idDevoirRendu).orElse(null);
	}


	@Transactional
	public List<byte[]> getDevoirsPDFs(Long idDevoir, String email) {
	    TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());
	    List<byte[]> pdfContents = new ArrayList<>();

	    try {
	        for (DevoirRendu d : this.getAllDevoirsRendu(idDevoir)) {
	            if (d.getEtudiant().getEmail().equals(email)) {
	                if (d != null && d.getPdfs() != null) {
	                    // Vérifier si d.getPdf() est une liste de byte[]
	                    if (d.getPdfs() instanceof List<?>) {
	                        // Cast de d.getPdf() en List<byte[]>
	                        List<byte[]> pdfList = (List<byte[]>) d.getPdfs();
	                        pdfContents.addAll(pdfList); // Ajouter tous les fichiers PDF à pdfContents
	                    } 
	                }
	            }
	        }
	    } finally {
	        transactionManager.commit(status);
	    }

	    return pdfContents;
	}


	@Override
	public void deleteDevoirRendu(Long idDevoir,String email) {
		 this.getAllDevoirsRendu(idDevoir).forEach(d -> {
		        if (d.getEtudiant().getEmail().equals(email)) {
		this.devoirRenduRepository.delete(d);
		        }});
		
	}
	
	@Transactional
	public DevoirRendu evaluerDevoir(EvaluationDTO evaluationDTO) {
	    DevoirRendu devoirRendu = devoirRenduRepository.findById(evaluationDTO.getIdDevoirRendu())
	            .orElseThrow(() -> new IllegalArgumentException("Devoir rendu non trouvé"));

	    devoirRendu.setNote(evaluationDTO.getNote());
	    devoirRendu.setCommentaire(evaluationDTO.getCommentaire());

	    

	    return devoirRenduRepository.save(devoirRendu);
	}




	
	public Optional<DevoirRendu> findByDevoirIdAndEtudiantEmail(Long idDevoir, String email) {
	    return devoirRenduRepository.findAll().stream()
	            .filter(dr -> dr.getDevoir().getIdDevoir().equals(idDevoir) && dr.getEtudiant().getEmail().equals(email))
	            .findFirst();
	}

}
