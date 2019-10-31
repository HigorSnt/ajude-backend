package org.ajude.controllers;

import org.ajude.Exceptions.EmailAlreadyRegisteredException;
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
    public ResponseEntity<User> createUser(@RequestBody User user) {
        try {
            return new ResponseEntity<User>(userService.createUser(user), HttpStatus.CREATED);
        } catch (EmailAlreadyRegisteredException e) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }
}
