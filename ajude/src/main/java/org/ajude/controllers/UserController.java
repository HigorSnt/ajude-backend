package org.ajude.controllers;

import org.ajude.dtos.UserNameEmail;
import org.ajude.entities.User;
import org.ajude.exceptions.EmailAlreadyRegisteredException;
import org.ajude.services.JwtService;
import org.ajude.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<UserNameEmail> createUser(@RequestBody User user) {
        try {
            return new ResponseEntity<>(this.userService.createUser(user), HttpStatus.CREATED);
        } catch (EmailAlreadyRegisteredException eare) {
            eare.printStackTrace();
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        } catch (MessagingException me) {
            me.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /*@DeleteMapping
    public ResponseEntity deleteComment(@RequestHeader("Authorization") String header,
                                        @RequestBody String idComment){

        try{
            String email = jwtService.getSubject(jwtService.getTokenUser(header));

            if(jwtService.userHasPermission(header, email))
                userService.deleteComment(userService.getUser(email).get(), idComment);

        } catch (ServletException e) {
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }

    }*/
}
