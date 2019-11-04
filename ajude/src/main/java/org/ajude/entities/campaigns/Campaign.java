package org.ajude.entities.campaigns;

import org.ajude.entities.users.User;
import org.ajude.utils.Status;

import java.util.Date;

public class Campaign {
    private Long id;
    private String acronym;
    private String URLIdentifier;
    private String description;
    private Date deadline;
    private Status status;
    private double goal;

    private User owner;

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

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public void setStatus(String s) {
        //TODO
    }
}