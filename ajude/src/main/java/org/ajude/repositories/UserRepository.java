package org.ajude.repositories;

import org.ajude.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.Optional;

@Repository
public interface UserRepository<T, ID extends Serializable> extends JpaRepository<User, String> {

    Optional<User> findByUsername(String username);

}
