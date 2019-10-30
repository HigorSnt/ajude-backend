package org.ajude.controllers;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.ajude.entities.users.User;
import org.ajude.entities.users.dtos.UserEmailPassword;
import org.ajude.services.JwtService;
import org.ajude.services.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class LoginController {

    @Value("${jwt.KEY}")
    private String KEY;

    private UserService userService;
    private JwtService jwtService;

    public LoginController() {
    }

    public LoginController(UserService userService, JwtService jwtService) {
        this.userService = userService;
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticate (@RequestBody UserEmailPassword user) {
        Optional<User> authUser = this.userService.getUser(user.getEmail());

        if (authUser.isEmpty()) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        if (!authUser.get().getPassword().equals(user.getPassword())) {
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }

        String token = Jwts.builder()
                .setSubject(authUser.get().getEmail())
                .signWith(SignatureAlgorithm.HS512, KEY)
                .setExpiration(new Date(System.currentTimeMillis() + 30 * 60 * 1000))
                .compact();

        return new ResponseEntity(new LoginResponse(token), HttpStatus.OK);
    }

    private class LoginResponse {
        public String token;

        public LoginResponse(String token) {
            this.token = token;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }
    }
}
