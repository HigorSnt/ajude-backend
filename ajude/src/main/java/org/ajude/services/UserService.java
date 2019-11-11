package org.ajude.services;

import org.ajude.dtos.UserProfile;
import org.ajude.entities.User;
import org.ajude.dtos.UserNameEmail;
import org.ajude.exceptions.EmailAlreadyRegisteredException;
import org.ajude.exceptions.NotFoundException;
import org.ajude.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.util.Optional;

@Service
public class UserService {

    private UserRepository<User, String> userRepository;
    private EmailService emailService;

    @Autowired
    public UserService(UserRepository<User, String> userRepository, EmailService emailService) {
        this.userRepository = userRepository;
        this.emailService = emailService;
    }

    public Optional<User> getUserByEmail(String email) {
        return this.userRepository.findById(email);
    }

    public UserProfile getUserByUsername(String username) throws NotFoundException {
        User u = this.userRepository.findByUsername(username).get();

        if (u != null) {
            return new UserProfile(u.getEmail(), u.getFirstName(),
                    u.getLastName(), u.getEmail());
        } else {
            throw new NotFoundException("The user " + username + " was not found.");
        }
    }

    public UserNameEmail createUser(User user) throws EmailAlreadyRegisteredException, MessagingException {
        if (getUserByEmail(user.getEmail()).isEmpty()) {
            this.emailService.sendWelcomeEmail(user.getEmail(), user.getFirstName());

            user.setUsername(user.getFirstName() + "." + user.getLastName() + "." + user.hashCode());

            this.userRepository.save(user);
            UserNameEmail userNameEmail = new UserNameEmail(
                    user.getEmail(),
                    user.getFirstName(),
                    user.getLastName(),
                    user.getUsername()
            );

            return userNameEmail;
        } else {
            throw new EmailAlreadyRegisteredException("Alredy exist a user with this email");
        }
    }

    public void forgotPassword(String email, String temporaryToken) throws MessagingException {
        Optional<User> user = getUserByEmail(email);

        this.emailService.sendForgotPasswordEmail(user.get().getEmail(),
                user.get().getFirstName(), temporaryToken);
    }

    public void resetPassword(String email, String newPassword) {
        Optional<User> optUser = getUserByEmail(email);
        if (!optUser.isEmpty()) {
            User user = optUser.get();

            user.setPassword(newPassword);
            this.userRepository.save(user);
        }
    }

    public void deleteComment(User user, String idComment) {
        //user.deleteComment(idComment);

        this.userRepository.save(user);
    }
}
