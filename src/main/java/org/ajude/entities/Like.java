package org.ajude.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.ajude.dtos.UserNameEmail;

import javax.persistence.*;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "like_table")
public class Like {

    @Id
    @GeneratedValue
    @Column(name = "idLike")
    private Long id;

    @ManyToOne
    @JsonIgnore
    private User likeUser;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserNameEmail getUser() {
        return new UserNameEmail(
                this.likeUser.getEmail(),
                this.likeUser.getFirstName(),
                this.likeUser.getLastName(),
                this.likeUser.getUsername()
        );
    }

    public void setLikeUser(User likeUser) {
        this.likeUser = likeUser;
    }

    public User getLikeUser() {
        return likeUser;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Like like = (Like) o;
        return likeUser.equals(like.likeUser);
    }

    @Override
    public int hashCode() {
        return Objects.hash(likeUser);
    }
}
