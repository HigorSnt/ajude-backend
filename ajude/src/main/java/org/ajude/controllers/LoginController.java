package org.ajude.controllers;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.ajude.entities.logins.Password;
import org.ajude.entities.users.User;
import org.ajude.entities.logins.Login;
import org.ajude.services.JwtService;
import org.ajude.services.UserService;
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
@RequestMapping("/auth")
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

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticate(@RequestBody Login user) {
        Optional<User> authUser = this.userService.getUser(user.getEmail());

        if (authUser.isEmpty()) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        if (!authUser.get().getPassword().equals(user.getPassword())) {
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }

        String token = buildToken(authUser.get().getEmail(), 30);

        return new ResponseEntity(new LoginResponse(token), HttpStatus.OK);
    }

    @PostMapping("/forgotPassword/{email}")
    public ResponseEntity<HttpStatus> forgotPassword(@PathVariable String email,
                                                     @RequestHeader("Authorization") String header) throws ServletException, MessagingException {

        if (header == null || !header.startsWith("Bearer ") || !this.jwtService.userHasPermission(header, email)) {

            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }

        String subject = this.jwtService.getTokenUser(header);

        String temporaryToken = buildToken(subject, 1);
        this.userService.forgotPassword(subject, temporaryToken);

        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("/resetPassword/{token}")
    public ResponseEntity<HttpStatus> resetPassword(@PathVariable String token,
                                                    @RequestBody Password newPassword) {
        try {
            String email = this.jwtService.getSubject(token);
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
