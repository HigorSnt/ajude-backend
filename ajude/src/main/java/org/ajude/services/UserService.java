package org.ajude.services;

import org.ajude.entities.users.dtos.UserNameEmail;
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
    private EmailService emailService;

    @Autowired
    public UserService(UserRepository<User, String> userRepository, EmailService emailService) {
        this.userRepository = userRepository;
        this.emailService = emailService;
    }

    public Optional<User> getUser(String email) {
        return this.userRepository.findById(email);
    }

    public UserNameEmail createUser(User user) throws EmailAlreadyRegisteredException, MessagingException {
        if (getUser(user.getEmail()).isEmpty()) {
            this.userRepository.save(user);

            UserNameEmail userNameEmail = new UserNameEmail();
            userNameEmail.setEmail(user.getEmail());
            userNameEmail.setFirstName(user.getFirstName());
            userNameEmail.setLastName(user.getLastName());

            this.emailService.sendWelcomeEmail(user.getEmail(), user.getFirstName());

            return userNameEmail;
        } else {
            throw new EmailAlreadyRegisteredException();
        }
    }

    public void forgotPassword(String email, String temporaryToken) throws MessagingException {
        Optional<User> user = getUser(email);

        this.emailService.sendForgotPasswordEmail(user.get().getEmail(),
                user.get().getFirstName(), temporaryToken);
    }

    public void resetPassword(String email, String newPassword) {
        Optional<User> optUser = getUser(email);
        if (!optUser.isEmpty()){
            User user = optUser.get();

            user.setPassword(newPassword);
            this.userRepository.save(user);
        }
    }
}
