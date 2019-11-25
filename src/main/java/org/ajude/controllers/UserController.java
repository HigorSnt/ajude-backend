package org.ajude.controllers;

import org.ajude.dtos.UserNameEmail;
import org.ajude.dtos.UserProfile;
import org.ajude.entities.User;
import org.ajude.exceptions.EmailAlreadyRegisteredException;
import org.ajude.exceptions.NotFoundException;
import org.ajude.services.JwtService;
import org.ajude.services.UserService;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;

@RestController
@RequestMapping("/user")
public class UserController {

    private UserService userService;
    private JwtService jwtService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<UserNameEmail> createUser(@RequestBody User user) throws EmailAlreadyRegisteredException, MessagingException {
        return new ResponseEntity<>(this.userService.createUser(user), HttpStatus.CREATED);
    }

    @PostMapping("/profile")
    public ResponseEntity<UserProfile> getUser(@RequestBody JSONObject json) throws NotFoundException {
        return new ResponseEntity(this.userService.getUserProfile(json.get("userEmail").toString()), HttpStatus.OK);
    }
}

