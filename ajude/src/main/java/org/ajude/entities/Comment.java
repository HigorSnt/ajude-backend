package org.ajude.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity
public class Comment {

    @Id
    @GeneratedValue
    private Long id;
    private String comment;
    private boolean isDeleted;

    @OneToOne(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.ALL})
    @JsonIgnore
    private Comment reply;

    @ManyToOne(optional = false, fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JsonIgnore
    private User owner;

    public Comment() {
    }

    public Comment(Long id, String comment, Comment reply, User owner) {
        this.id = id;
        this.comment = comment;
        this.reply = reply;
        this.owner = owner;
        this.isDeleted = false;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getComment() {
        return comment;
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

    public User getOwner() {
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
}
