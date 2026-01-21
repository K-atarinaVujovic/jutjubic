package com.jutjubiccorps.jutjubic.service;

import com.jutjubiccorps.jutjubic.model.User;
import jakarta.annotation.PostConstruct;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender javaMailSender;

    @Value("${app.frontend.url}")
    private String frontendUrl;

    /*
     * Koriscenje klase za ocitavanje vrednosti iz application.properties fajla
     */
//    private final Environment env;

    public EmailService (JavaMailSender javaMailSender, Environment env){
        this.javaMailSender = javaMailSender;
    }

    @PostConstruct
    public void testConnection() throws MessagingException
    { ((JavaMailSenderImpl) javaMailSender).testConnection(); }

    /*
     * Anotacija za oznacavanje asinhronog zadatka
     * Vise informacija na: https://docs.spring.io/spring/docs/current/spring-framework-reference/integration.html#scheduling
     */
    @Async
    public void sendNotificationAsync(User user) throws MailException, InterruptedException {
        String subject = "Jutjubic activation link";
        String link = frontendUrl + "/activate?token=" + user.getValidationToken();
        String body = "Here is your link: " + link;
        sendEmail(user.getEmail(), subject, body);
    }

    private void sendEmail(String to, String subject, String body) {
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setTo(to);
        mail.setSubject(subject);
        mail.setText(body);
        javaMailSender.send(mail);
    }
}
