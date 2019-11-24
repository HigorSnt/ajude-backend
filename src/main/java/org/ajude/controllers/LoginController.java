package org.ajude.controllers;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.ajude.entities.Login;
import org.ajude.entities.Password;
import org.ajude.entities.User;
import org.ajude.services.JwtService;
import org.ajude.services.UserService;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.servlet.ServletException;
import java.util.Date;
import java.util.Optional;

@RestController
public class LoginController {

    @Value("${jwt.key}")
    private String key;

    private UserService userService;
    private JwtService jwtService;

    @Autowired
    public LoginController(UserService userService, JwtService jwtService) {
        this.userService = userService;
        this.jwtService = jwtService;
    }

    @PostMapping("auth/login")
    public ResponseEntity<LoginResponse> authenticate(@RequestBody Login user) {
        Optional<User> authUser = this.userService.getUserByEmail(user.getEmail());

        if (authUser.isEmpty()) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        if (!authUser.get().getPassword().equals(user.getPassword())) {
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }

        String token = buildToken(authUser.get().getEmail(), 30);

        return new ResponseEntity(new LoginResponse(token), HttpStatus.OK);
    }

    @PostMapping("/forgotPassword")
    public ResponseEntity<HttpStatus> forgotPassword(@RequestBody JSONObject json)
            throws ServletException, MessagingException {

        String email = json.get("email").toString();

        if (this.userService.getUserByEmail(email).get() != null) {
            String temporaryToken = buildToken(email, 1);
            this.userService.forgotPassword(email, temporaryToken);

            return new ResponseEntity(HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/resetPassword/{token}")
    public ResponseEntity<HttpStatus> resetPassword(@PathVariable String token,
                                                    @RequestBody Password newPassword) {
        try {
            String email = this.jwtService.getSubjectByToken(token);
            this.userService.resetPassword(email, newPassword.getPassword());

            return new ResponseEntity(HttpStatus.OK);
        } catch (ExpiredJwtException eje) {
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }
    }

    private String buildToken(String email, Integer minutes) {
        return Jwts.builder()
                .setSubject(email)
                .signWith(SignatureAlgorithm.HS512, this.key)
                .setExpiration(new Date(System.currentTimeMillis() + minutes * 60 * 1000))
                .compact();
    }

    private class LoginResponse {
        public String token;

        public LoginResponse(String token) {
            this.token = token;
        }

        public String getToken() {
            return this.token;
        }

        public void setToken(String token) {
            this.token = token;
        }
    }
}
