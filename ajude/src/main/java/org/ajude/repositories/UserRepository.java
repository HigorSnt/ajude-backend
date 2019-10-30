package org.ajude.repositories;

import org.ajude.entities.users.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.io.Serializable;

@Repository
public interface UserRepository<T, ID extends Serializable> extends JpaRepository<User, String> {

}
