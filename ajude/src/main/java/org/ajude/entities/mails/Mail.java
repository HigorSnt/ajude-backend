package org.ajude.entities.mails;

import java.util.Map;

public class Mail {

    private String to;
    private String subject;
    private String text;
    private Map<String, Object> model;

    public Mail() {
    }

    public Mail(String to, String subject, String text, Map<String, Object> model) {
        this.to = to;
        this.subject = subject;
        this.text = text;
        this.model = model;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public Map<String, Object> getModel() {
        return model;
    }

    public void setModel(Map<String, Object> model) {
        this.model = model;
    }
}
