package org.ajude.services;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureException;
import org.ajude.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.ServletException;
import java.util.Optional;

@Service
public class JwtService {

    private UserService userService;

    @Value("${jwt.tokenIndex}")
    private Integer tokenIndex;

    @Value("${jwt.key}")
    private String key;

    @Autowired
    public JwtService(UserService userService) {
        this.userService = userService;
    }

    public boolean userHasPermission(String authorizationHeader, String email) throws ServletException {
        String subject = getSubjectByHeader(authorizationHeader);

        Optional<User> optionalUser = this.userService.getUserByEmail(subject);

        return optionalUser.isPresent() && optionalUser.get().getEmail().equals(email);
    }

    public String getSubjectByHeader(String authorizationHeader) throws ServletException {
        validateHeader(authorizationHeader);

        String token = authorizationHeader.substring(this.tokenIndex);

        String subject = null;

        try {
            subject = getSubjectByToken(token);
        } catch (SignatureException se) {
            throw new ServletException("INVALID OR EXPIRATED TOKEN");
        }

        return subject;
    }

    public String getSubjectByToken(String token) {
        return Jwts.parser()
                .setSigningKey(this.key)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    private void validateHeader(String header) throws ServletException {
        if (header == null || !header.startsWith("Bearer ")) {
            throw new ServletException("MISSING OR BADLY FORMED TOKEN");
        }
    }

}
