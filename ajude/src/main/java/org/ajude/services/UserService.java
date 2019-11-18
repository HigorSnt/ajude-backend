package org.ajude.services;

import org.ajude.dtos.UserNameEmail;
import org.ajude.dtos.UserProfile;
import org.ajude.entities.User;
import org.ajude.exceptions.EmailAlreadyRegisteredException;
import org.ajude.exceptions.NotFoundException;
import org.ajude.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.text.Normalizer;
import java.util.Optional;
import java.util.regex.Pattern;

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

            user.setUsername(this.createUsername(user.getFirstName().toLowerCase(), user.getLastName().toLowerCase()) +
                    "." + Math.abs(user.hashCode()));

            this.userRepository.save(user);

            UserNameEmail userNameEmail = new UserNameEmail(
                    user.getEmail(),
                    user.getFirstName(),
                    user.getLastName(),
                    user.getUsername()
            );

            return userNameEmail;
        } else {
            throw new EmailAlreadyRegisteredException("Alredy exist an user with this email");
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

    private String createUsername(String firstName, String lastName) {
        String normalizedFirstName = Normalizer.normalize(firstName, Normalizer.Form.NFD)
                .replace(" ", ".");
        String normalizedLastName = Normalizer.normalize(lastName, Normalizer.Form.NFD)
                .replace(" ", ".");
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");

        String username = pattern.matcher(normalizedFirstName).replaceAll("") +
                "." + pattern.matcher(normalizedLastName).replaceAll("");

        return username;
    }

}
