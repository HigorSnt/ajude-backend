package org.ajude.services;

import org.ajude.entities.Mail;
import org.apache.commons.io.IOUtils;
import org.javatuples.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Service
public class EmailService {

    private JavaMailSender mailSender;
    private SpringTemplateEngine engine;
    private Pair<InputStreamSource, MultipartFile> ajude;
    private Pair<InputStreamSource, MultipartFile> facebook;
    private Pair<InputStreamSource, MultipartFile> instagram;

    @Autowired
    public EmailService(JavaMailSender mailSender,
                        @Qualifier("springTemplateEngine") SpringTemplateEngine engine) throws IOException {
        this.mailSender = mailSender;
        this.engine = engine;
        this.ajude = getImageContent("ajude.png");
        this.facebook = getImageContent("facebook.png");
        this.instagram = getImageContent("instagram.png");
    }

    public void sendWelcomeEmail(String to, String name) throws MessagingException {
        Mail mail = new Mail();

        mail.setTo(to);
        mail.setSubject("Bem-vindo ao AJuDE");

        Map<String, Object> model = new HashMap();
        model.put("name", name);
        model.put("ajude", this.ajude.getValue1().getName());
        model.put("facebook", this.facebook.getValue1().getName());
        model.put("instagram", this.instagram.getValue1().getName());

        mail.setModel(model);
        sendEmail(mail, "welcome-mail");
    }

    public void sendForgotPasswordEmail(String email, String firstName,
                                        String temporaryToken) throws MessagingException {

        Mail mail = new Mail();
        mail.setTo(email);
        mail.setSubject("Recuperação de Senha");

        Map<String, Object> model = new HashMap<>();
        model.put("name", firstName);
        model.put("token", temporaryToken);
        model.put("ajude", this.ajude.getValue1().getName());
        model.put("facebook", this.facebook.getValue1().getName());
        model.put("instagram", this.instagram.getValue1().getName());

        mail.setModel(model);
        sendEmail(mail, "forgot-password");
    }

    private void sendEmail(Mail mail, String template) throws MessagingException {
        Context context = new Context();
        context.setVariables(mail.getModel());

        String templateHTML = this.engine.process(template, context);
        MimeMessage message = this.mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                StandardCharsets.UTF_8.name());

        helper.setTo(mail.getTo());
        helper.setSubject(mail.getSubject());
        helper.setText(templateHTML, true);

        helper.addInline(this.ajude.getValue1().getName(), this.ajude.getValue0(),
                this.ajude.getValue1().getContentType());
        helper.addInline(this.facebook.getValue1().getName(), this.facebook.getValue0(),
                this.facebook.getValue1().getContentType());
        helper.addInline(this.instagram.getValue1().getName(), this.instagram.getValue0(),
                this.instagram.getValue1().getContentType());

        this.mailSender.send(message);
    }

    private Pair<InputStreamSource, MultipartFile> getImageContent(String image) throws IOException {
        InputStream inputStream = null;
        InputStreamSource source = null;
        byte[] imageByteArray = null;
        MultipartFile multipartFile = null;

        inputStream = this.getClass().getClassLoader().getResourceAsStream("static/images/" + image);
        imageByteArray = IOUtils.toByteArray(inputStream);
        multipartFile = new MockMultipartFile(image.substring(0, image.length() - 4),
                inputStream.getClass().getName(),
                "image/png", imageByteArray);
        source = new ByteArrayResource(imageByteArray);

        return new Pair(source, multipartFile);
    }

}
