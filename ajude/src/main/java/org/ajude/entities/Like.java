package org.ajude.entities;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "like_table")
public class Like {

    @Id
    @GeneratedValue
    private Long id;
    @ManyToOne
    private User user;

    public Like(User user) {
        this.user = user;
    }

    public Like() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Like like = (Like) o;
        return user.equals(like.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user);
    }
}
