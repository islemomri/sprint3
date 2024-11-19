package com.project.app.services.jwt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;


@Service
public class EmailService {
	@Autowired
    private JavaMailSender mailSender;
	
	public void sendInvitationEmail(String to, String inviteLink, String code) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject("Invitation à rejoindre un cours");
            message.setText("Vous avez été invité à rejoindre un cours. Utilisez le code suivant : " + code +
                           "\nOu cliquez sur le lien pour rejoindre : " + inviteLink);

            mailSender.send(message);
            System.out.println("Email envoyé avec succès à : " + to);
        } catch (Exception e) {
            System.err.println("Erreur lors de l'envoi de l'e-mail : " + e.getMessage());
            e.printStackTrace();
        }
    }

	
    

}
