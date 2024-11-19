package com.project.app.controllers;


import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.app.services.jwt.CourService;

@SpringBootTest
@AutoConfigureMockMvc
public class InviterEnseignantControllerTest {
	
	private MockMvc mockMvc;

    @Mock
    private CourService courService;

    @InjectMocks
    private CourController courController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(courController).build();
    }
    
    @Test
    public void testInviteTeacherByEmail_Success() throws Exception {
        when(courService.inviteTeacherByEmail(anyString(), anyString())).thenReturn(true);

        Map<String, String> request = new HashMap<>();
        request.put("teacherEmail", "teacher@example.com");

        MvcResult result = mockMvc.perform(post("/courseCode/inviteTeacherByEmail")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isOk())
                .andReturn();

        assertEquals("L'enseignant a été invité avec succès.", result.getResponse().getContentAsString());
    }

    @Test
    public void testInviteTeacherByEmail_Failure() throws Exception {
        when(courService.inviteTeacherByEmail(anyString(), anyString())).thenReturn(false);

        Map<String, String> request = new HashMap<>();
        request.put("teacherEmail", "teacher@example.com");

        MvcResult result = mockMvc.perform(post("/courseCode/inviteTeacherByEmail")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andReturn();

        assertEquals("Impossible d'inviter l'enseignant.", result.getResponse().getContentAsString());
    }


}
