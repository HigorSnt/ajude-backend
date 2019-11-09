package org.ajude.entities;

import org.ajude.utils.Status;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.Instant;
import java.util.Date;

@Entity
public class Campaign {

    @Id
    @GeneratedValue
    private Long id;
    private String shortName;
    private String urlIdentifier;
    private String description;
    private Date deadline;
    private Status status;
    private Double goal;
    private String ownerEmail;

    public Campaign(String shortName, String urlIdentifier,
                    String description, Date deadline, Status status,
                    Double goal, String ownerEmail) {

        this.shortName = shortName;
        this.urlIdentifier = urlIdentifier;
        this.description = description;
        this.deadline = deadline;
        this.status = status;
        this.goal = goal;
        this.ownerEmail = ownerEmail;
    }

    public Campaign() {
    }

    public void verifyDeadline() {
        //TODO Uma campanha se torna vencida quando o deadline
        // configurado para atingir a meta chegou e a meta não foi atingida.
        // Finalmente, uma campanha é marcada como concluida
        // quando ela atingir a meta e o deadline
        if (this.deadline.before(Date.from(Instant.now()))) {
            this.setStatus(Status.E);
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Double getGoal() {
        return goal;
    }

    public void setGoal(Double goal) {
        this.goal = goal;
    }

    public String getOwnerEmail() {
        return ownerEmail;
    }

    public void setOwnerEmail(String ownerEmail) {
        this.ownerEmail = ownerEmail;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Campaign campaign = (Campaign) o;

        if (!id.equals(campaign.id)) return false;
        return shortName.equals(campaign.shortName);
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + shortName.hashCode();
        return result;
    }
}