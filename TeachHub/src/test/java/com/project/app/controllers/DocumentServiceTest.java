package com.project.app.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import com.project.app.models.Cour;
import com.project.app.models.Document;
import com.project.app.models.Enseignant;
import com.project.app.repository.CourRepository;
import com.project.app.repository.DocumentRepository;
import com.project.app.repository.EnseignantRepository;
import com.project.app.services.jwt.CourService;

@SpringBootTest
@AutoConfigureMockMvc
public class DocumentServiceTest {

    @Autowired
    private MockMvc mockMvc;

    @InjectMocks
    private CourService courService; 

    @Mock
    private DocumentRepository documentRepository;

    @Mock
    private CourRepository courRepository;

    @Mock
    private EnseignantRepository enseignantRepository;

    @Mock
    private MockMultipartFile file;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testUploadDocument() throws Exception {
        Integer courId = 1;
        Long enseignantId = 1L;
        String fileName = "devoir_1.pdf";
        String filePath = "uploads/" + fileName;

        // Simuler le fichier
        byte[] fileContent = "Document content".getBytes();
        file = new MockMultipartFile("file", fileName, MediaType.APPLICATION_PDF_VALUE, fileContent);

        // Simuler la récupération du cours et de l'enseignant
        Cour cour = new Cour();
        cour.setIdCours(courId);
        Enseignant enseignant = new Enseignant();
        enseignant.setId(enseignantId);

        when(courRepository.findById(courId)).thenReturn(java.util.Optional.of(cour));
        when(enseignantRepository.findById(enseignantId)).thenReturn(java.util.Optional.of(enseignant));

        // Simuler la sauvegarde du document
        Path copyLocation = Paths.get(filePath);
        Document savedDocument = new Document(fileName, file.getContentType(), copyLocation.toString(), cour, enseignant);
        when(documentRepository.save(any(Document.class))).thenReturn(savedDocument);

        // Ajoutez des logs pour déboguer les erreurs
        
    }
}
