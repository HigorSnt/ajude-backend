package org.ajude.services;

import org.ajude.entities.mails.Mail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class EmailService {

    @Autowired
    JavaMailSender mailSender;

    @Autowired
    private TemplateEngine engine;

    public void sendWelcomeEmail(String to, String name) throws MessagingException {
        Mail mail = new Mail();

        mail.setTo(to);
        mail.setSubject("Bem-vindo ao AJuDE");
        mail.setText("Seja muito bem-vindo, " + name + "!");

        Map<String, Object> model = new HashMap();
        model.put("name", name);
        model.put("text", mail.getText());
        model.put("date",
                new SimpleDateFormat("dd/MM/yyyy").format(new Date()));
        model.put("location", "Campina Grande, Brasil");

        mail.setModel(model);

        sendEmail(mail);
    }

    private void sendEmail(Mail mail) throws MessagingException {
        MimeMessage message = this.mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        Context context = new Context();
        context.setVariables(mail.getModel());
        
        String templateHTML = engine.process("welcome-mail", context);

        helper.setTo(mail.getTo());
        helper.setSubject(mail.getSubject());
        helper.setText(templateHTML, true);

        this.mailSender.send(message);
    }


}
