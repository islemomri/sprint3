package com.project.app.controllers;



import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.project.app.dto.CourDTO;
import com.project.app.models.Cour;
import com.project.app.models.Enseignant;
import com.project.app.repository.CourRepository;
import com.project.app.repository.EnseignantRepository;
import com.project.app.services.jwt.CourService;

@SpringBootTest
public class CourServiceTest {

    @Autowired
    private CourService courService;

    @MockBean
    private CourRepository courRepository;

    @MockBean
    private EnseignantRepository enseignantRepository;

    @Test
    public void testAddCour_Success() {
        // Données de test
        CourDTO courDTO = new CourDTO(1, "Mathématiques", 2.5f, 4);
        Enseignant enseignant = new Enseignant();
        enseignant.setEmail("ens@universite.com");

        // Simuler le retour de l'enseignant
        when(enseignantRepository.findByEmail(anyString())).thenReturn(Optional.of(enseignant));
        when(courRepository.save(any(Cour.class))).thenReturn(new Cour(1, "Mathématiques", 2.5f, 4));

        // Appel de la méthode
        Cour savedCour = courService.addCour(courDTO, "ens@universite.com");

        // Vérifications
        assertNotNull(savedCour);
        assertEquals("Mathématiques", savedCour.getNom());
        assertEquals(2.5f, savedCour.getCoefficient(), 0.01);
        assertEquals(4, savedCour.getCredits());
    }
    
    
    

}
