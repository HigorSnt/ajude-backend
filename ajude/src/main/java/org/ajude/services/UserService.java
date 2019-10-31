package org.ajude.services;

import org.ajude.exceptions.EmailAlreadyRegisteredException;
import org.ajude.entities.users.User;
import org.ajude.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.util.Optional;

@Service
public class UserService {

    private UserRepository<User, String> userRepository;
    @Autowired
    private EmailService emailService;

    public UserService(UserRepository<User, String> userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<User> getUser(String email) {
        return this.userRepository.findById(email);
    }

    public User createUser(User user) throws EmailAlreadyRegisteredException, MessagingException {
        if (getUser(user.getEmail()).isEmpty()) {
            this.emailService.sendWelcomeEmail(user.getEmail(), user.getFirstName());
            this.userRepository.save(user);
            return user;
        } else {
            throw new EmailAlreadyRegisteredException();
        }
    }
}
