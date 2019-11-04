package org.ajude.entities.campaigns;

import org.ajude.utils.Status;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;
@Entity
public class Campaign {
    @Id
    @GeneratedValue
    private Long id;
    private String acronym;
    private String URLIdentifier;
    private String description;
    private Date deadline;
    private Status status;
    private double goal;

    private String ownerEmail;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAcronym() {
        return acronym;
    }

    public void setAcronym(String acronym) {
        this.acronym = acronym;
    }

    public String getURLIdentifier() {
        return URLIdentifier;
    }

    public void setURLIdentifier(String URLIdentifier) {
        this.URLIdentifier = URLIdentifier;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDeadine() {
        return deadline;
    }

    public void setDeadine(Date deadline) {
        this.deadline = deadline;
    }

    public double getGoal() {
        return goal;
    }

    public void setGoal(double goal) {
        this.goal = goal;
    }

    public void setStatus(String s) {
        //TODO
    }

    public String getOwnerEmail() {
        return ownerEmail;
    }

    public void setOwnerEmail(String ownerEmail) {
        this.ownerEmail = ownerEmail;
    }
}