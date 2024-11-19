package com.project.app.services.jwt;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.app.models.Cour;
import com.project.app.models.Document;
import com.project.app.repository.DocumentRepository;

@Service
public class DocumentService {
	
	@Autowired
	private DocumentRepository documentRepository;
	
	
	
}
