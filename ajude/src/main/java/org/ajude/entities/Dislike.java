package org.ajude.entities;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "dislike_table")
public class Dislike {
    @Id
    @GeneratedValue
    private Long id;
    @ManyToOne
    private User user;

    public Dislike(User user) {
        this.user = user;
    }

    public Dislike() {
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
        Dislike dislike = (Dislike) o;
        return user.equals(dislike.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user);
    }
}
