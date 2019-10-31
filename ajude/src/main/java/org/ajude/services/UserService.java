package org.ajude.services;

import org.ajude.Exceptions.EmailAlreadyRegisteredException;
import org.ajude.entities.users.User;
import org.ajude.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private UserRepository<User, String> userRepository;

    public UserService(UserRepository<User, String> userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<User> getUser(String email) {
        return this.userRepository.findById(email);
    }

    public User createUser(User user) throws EmailAlreadyRegisteredException {
        if (getUser(user.getEmail()).isEmpty()) {
            //TODO ENVIA EMAIL
            this.userRepository.save(user);
            return user;
        } else {
            throw new EmailAlreadyRegisteredException();
        }
    }
}
