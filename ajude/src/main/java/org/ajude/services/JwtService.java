package org.ajude.services;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureException;
import org.ajude.entities.users.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.ServletException;
import java.util.Optional;

@Service
public class JwtService {

    private UserService userService;

    @Value("${jwt.tokenIndex}")
    private Integer TOKEN_INDEX;

    @Value("${jwt.key}")
    private String KEY;

    public JwtService() {
    }

    public JwtService(UserService userService) {
        this.userService = userService;
    }

    public boolean userHasPermission(String authorizationHeader, String email) throws ServletException {
        String subject = getTokenUser(authorizationHeader);

        Optional<User> optionalUser = this.userService.getUser(subject);

        return optionalUser.isPresent() && optionalUser.get().getEmail().equals(email);
    }

    public String getTokenUser (String authorizationHeader) throws ServletException {
        validateHeader(authorizationHeader);

        String token = authorizationHeader.substring(TOKEN_INDEX);
        String subject = null;

        try {
            subject = Jwts.parser()
                    .setSigningKey(KEY)
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
        } catch (SignatureException se) {
            throw new ServletException("INVALID OR EXPIRATED TOKEN");
        }

        return subject;
    }

    private void validateHeader (String header) throws ServletException {
        if (header == null || !header.startsWith("Bearer ")) {
            throw new ServletException("MISSING OR BADLY FORMED TOKEN");
        }
    }

}
