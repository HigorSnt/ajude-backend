package org.ajude.controllers;

import org.ajude.entities.users.dtos.UserNameEmail;
import org.ajude.exceptions.EmailAlreadyRegisteredException;
import org.ajude.entities.users.User;
import org.ajude.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/user")
    public ResponseEntity<UserNameEmail> createUser(@RequestBody User user) {
        try {
            return new ResponseEntity<UserNameEmail>(userService.createUser(user), HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }
}
