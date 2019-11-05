package org.ajude.services;

import org.ajude.entities.mails.Mail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class EmailService {

    private JavaMailSender mailSender;
    private SpringTemplateEngine engine;

    @Autowired
    public EmailService(JavaMailSender mailSender,
                        @Qualifier("springTemplateEngine") SpringTemplateEngine engine) {
        this.mailSender = mailSender;
        this.engine = engine;
    }

    public void sendWelcomeEmail(String to, String name) throws MessagingException {
        Mail mail = new Mail();

        mail.setTo(to);
        mail.setSubject("Bem-vindo ao AJuDE");

        Map<String, Object> model = new HashMap();
        model.put("name", name);
        model.put("ajudeLogo", "ajude");
        model.put("facebookLogo", "facebook");
        model.put("instagramLogo", "instagram");

        mail.setModel(model);

        sendEmail(mail, "welcome-mail");
    }

    private void sendEmail(Mail mail, String template) throws MessagingException {
        MimeMessage message = this.mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        Context context = new Context();
        context.setVariables(mail.getModel());

        helper.setTo(mail.getTo());
        helper.setSubject(mail.getSubject());

        String templateHTML = engine.process(template, context);
        helper.setText(templateHTML, true);

        /*
        InputStreamSource ajudeSource = new ByteArrayResource(convertImageToByte("images/ajude.svg"));
        InputStreamSource facebookSource = new ByteArrayResource(convertImageToByte("images/facebook.svg"));
        InputStreamSource instagramSource = new ByteArrayResource(convertImageToByte("images/instagram.svg"));

        helper.addInline("ajudeLogo", ajudeSource, "image/svg");
        helper.addInline("facebookLogo", facebookSource, "image/svg");
        helper.addInline("instagramLogo", instagramSource, "image/svg");*/

        this.mailSender.send(message);
    }

    private byte[] convertImageToByte (String pathImage) {
        File file = new File(pathImage);
        byte[] image = new byte[(int) file.length()];
        FileInputStream fileInputStream = null;

        try {
            fileInputStream = new FileInputStream(file);
            fileInputStream.read(image);
            fileInputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return image;
    }


    public void sendForgotPasswordEmail(String email, String firstName,
                                        String temporaryToken) throws MessagingException {

        Mail mail = new Mail();
        mail.setTo(email);
        mail.setSubject("Recuperação de Senha");
        mail.setText("Olá, " + firstName + " foi solicitado a recuperação da sua senha. " +
                "Se não foi você, desconsidere este email!");

        Map<String, Object> model = new HashMap<>();
        model.put("name", firstName);
        model.put("link", "localhost:8080/api/auth/resetPassword/" + temporaryToken);
        model.put("text", mail.getText());
        model.put("date",
                new SimpleDateFormat("dd/MM/yyyy").format(new Date()));
        model.put("location", "Campina Grande, Brasil");

        mail.setModel(model);

        sendEmail(mail, "forgot-password-template");
    }
}
