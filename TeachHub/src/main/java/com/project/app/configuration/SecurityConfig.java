package com.project.app.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.project.app.filters.JwtRequestFilter;



@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
	
	private final JwtRequestFilter jwtRequestFilter;

	   
	@Autowired
    public SecurityConfig(JwtRequestFilter jwtRequestFilter ) {
		this.jwtRequestFilter=jwtRequestFilter;
	}
	
	@Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity security) throws Exception {
        return security.cors().and().csrf().disable() // Désactive CSRF car nous utilisons des tokens
                .authorizeHttpRequests()
                .requestMatchers("/signup/**").permitAll()
                .requestMatchers("/login/**").permitAll()
                .requestMatchers("/api/etudiant/**").hasRole("ETUDIANT") // Accès uniquement pour les étudiants
                .requestMatchers("/api/enseignant/**").hasRole("ENSEIGNANT") // Accès uniquement pour les enseignants
                .anyRequest().permitAll() // Toutes les autres requêtes nécessitent une authentification
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // Utilise des sessions stateless
                .and()
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class) // Ajoute le filtre JWT
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // Utilise BCrypt pour le hachage des mots de passe
    }
}
