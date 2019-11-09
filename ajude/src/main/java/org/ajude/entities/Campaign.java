package org.ajude.entities;

import org.ajude.exceptions.CommentNotFoundException;
import org.ajude.utils.Status;

import javax.persistence.*;
import java.time.Instant;
import java.util.Date;
import java.util.List;

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

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "idUser")
    private User owner;

    @OneToMany(fetch = FetchType.EAGER)
    private List<Comment> comments;

    public Campaign(String shortName, String urlIdentifier, String description,
                    Date deadline, Status status, Double goal, User owner, List<Comment> comments) {
        this.shortName = shortName;
        this.urlIdentifier = urlIdentifier;
        this.description = description;
        this.deadline = deadline;
        this.status = status;
        this.goal = goal;
        this.owner = owner;
        this.comments = comments;
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

    public Comment addComment(Comment comment) {
        this.comments.add(comment);

        return comment;
    }

    public Comment addCommentResponse(Long commentId, Comment reply) throws CommentNotFoundException {
        Comment comment = this.comments.stream().filter(c -> c.getId() == commentId).findAny().get();

        if (comment != null) {
            comment.setReply(reply);
        } else {
            throw new CommentNotFoundException();
        }

        return comment;
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

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
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