package org.ajude.services;

import org.ajude.entities.User;
import org.ajude.repositories.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private UserRepository<User, String> userRepository;

    public UserService(UserRepository<User, String> userRepository) {
        this.userRepository = userRepository;
    }
}
