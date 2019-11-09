package org.ajude.entities;

import javax.persistence.*;

@Entity
public class Comment {

    @Id
    @GeneratedValue
    private Long id;
    private String comment;

    @OneToOne(fetch = FetchType.EAGER)
    private Comment reply;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private User owner;

    public Comment() {
    }

    public Comment(Long id, String comment, Comment reply, User owner) {
        this.id = id;
        this.comment = comment;
        this.reply = reply;
        this.owner = owner;
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

    public Comment getReply() {
        return reply;
    }

    public void setReply(Comment reply) {
        this.reply = reply;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }
}
