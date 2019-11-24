package org.ajude.dtos;

import org.ajude.entities.User;
import org.ajude.utils.Status;

import java.util.Date;

public class CampaignDTO {
    private String shortName;
    private String urlIdentifier;
    private String description;
    private Date deadline;
    private Double goal;
    private User owner;

    public CampaignDTO(String shortName, String urlIdentifier, String description, Date deadline, Double goal, User owner) {
        this.shortName = shortName;
        this.urlIdentifier = urlIdentifier;
        this.description = description;
        this.deadline = deadline;
        this.goal = goal;
        this.owner = owner;
    }

    public CampaignDTO() {
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getUrlIdentifier() {
        return urlIdentifier;
    }

    public void setUrlIdentifier(String urlIdentifier) {
        this.urlIdentifier = urlIdentifier;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDeadline() {
        return deadline;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }

    public Double getGoal() {
        return goal;
    }

    public void setGoal(Double goal) {
        this.goal = goal;
    }
}
