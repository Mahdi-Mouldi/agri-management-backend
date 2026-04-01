package com.agri.agrimanager.service;

import com.agri.agrimanager.entity.VerificationToken;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender mailSender;

    public void sendVerificationMail(String toEmail, String token){
        String subject = "Vérification de votre adresse email";
        String verificationUrl = "http://localhost:4200/verify-email?token=" + token;
        String body = "Bonjour,\n\n"
                + "Cliquez sur le lien suivant pour vérifier votre email :\n"
                + verificationUrl
                + "\n\nSi ce n'est pas vous, ignorez ce message.";
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject(subject);
        message.setText(body);
        mailSender.send(message);
    }
}
