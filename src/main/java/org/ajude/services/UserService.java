package org.ajude.services;

import org.ajude.dtos.CampaignDTO;
import org.ajude.dtos.UserNameEmail;
import org.ajude.dtos.UserProfile;
import org.ajude.entities.Campaign;
import org.ajude.entities.User;
import org.ajude.exceptions.EmailAlreadyRegisteredException;
import org.ajude.exceptions.NotFoundException;
import org.ajude.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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

    public UserProfile getUserProfile(String userEmail) throws NotFoundException {
        Optional<User> optionalUser = this.getUserByEmail(userEmail);
        if (optionalUser.isEmpty()){
            throw new NotFoundException("USER " + userEmail + " WAS NOT FOUND");
        }

        User u = optionalUser.get();
        List<CampaignDTO> campaignList = new ArrayList<>();

        for (Campaign c : u.getCampaigns())
            campaignList.add(new CampaignDTO(c.getShortName(), c.getUrlIdentifier(), c.getDescription(),
                    c.getDeadline(), c.getRegisterDateTime(), c.getStatus(),c.getGoal(), c.getReceived(),
                    c.getNumLikes(), c.getNumDislikes()));

        Collections.reverse(campaignList);
        return new UserProfile(u.getEmail(), u.getFirstName(), u.getLastName(), u.getUsername(), campaignList, u.getDonations());
    }
}
