package org.ajude.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.ajude.dtos.UserNameEmail;

import javax.persistence.*;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Comment {

    @Id
    @GeneratedValue
    private Long id;
    private String comment;
    private boolean isDeleted;

    @JsonIgnore
    private ZonedDateTime postedAt;

    @OneToOne(fetch = FetchType.EAGER,
            cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.ALL})
    private Comment reply;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JsonIgnore
    private User owner;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getComment() {
        return isDeleted ? "" : comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public Comment getReply() {
        return reply;
    }

    public void setReply(Comment reply) {
        this.reply = reply;
    }

    public void delete() {
        this.isDeleted = true;
    }

    public UserNameEmail getUser() {
        return new UserNameEmail(
                this.owner.getEmail(),
                this.owner.getFirstName(),
                this.owner.getLastName(),
                this.owner.getUsername()
        );
    }

    private User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public int recursiveDelete(User owner, Long idComment) {
        if (this.id.equals(idComment) && owner.equals(this.owner)) {
            this.delete();
            return 1;
        } else if (this.reply != null) reply.recursiveDelete(owner, idComment);
        return 0;
    }

    public String getPostedDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return this.postedAt.minusHours(3).format(formatter);
    }

    public void setPostedAt(ZonedDateTime postedAt) {
        this.postedAt = postedAt;
    }

    public ZonedDateTime getPostedAt() {
        return this.postedAt;
    }
}
