package com.project.app.controllers;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.app.dto.LoginRequest;
import com.project.app.dto.LoginResponse;
import com.project.app.services.jwt.UserServiceImplement;
import com.project.app.utils.JwtUtil;

import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/login")
public class LoginController {
	private final AuthenticationManager authenticationManager;
    private final UserServiceImplement userServiceImplement; 
    private final JwtUtil jwtUtil;
    
    @Autowired
    public LoginController(AuthenticationManager authenticationManager, UserServiceImplement userServiceImplement, JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.userServiceImplement = userServiceImplement; 
        this.jwtUtil = jwtUtil;
    }
    
    @PostMapping
    public LoginResponse login(@RequestBody LoginRequest loginRequest, HttpServletResponse response) throws IOException {
        try {
            // Authentifier l'utilisateur
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getMotDePasse())
            );
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Email ou mot de passe incorrect.");
        } catch (DisabledException disabledException) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "L'utilisateur n'est pas activé");
            return null;
        }
        String role;

        // Charger l'utilisateur via UserServiceImplement
        final UserDetails userDetails = userServiceImplement.loadUserByUsername(loginRequest.getEmail());
        final String jwt = jwtUtil.generateToken(userDetails.getUsername());
        if(userDetails.getAuthorities().toString().equals("[ROLE_ETUDIANT]")){
       	 role="etudiant";
       }else {
       	role="enseignant";
       }
        return new LoginResponse(jwt,role,this.userServiceImplement.getIdUser(loginRequest.getEmail()));
    }

}
