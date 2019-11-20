package org.ajude.filters;

import io.jsonwebtoken.*;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class TokenFilter extends GenericFilterBean {

    private final String KEY = "!GFLWasIzyuc4K0Ge7vQxqj^RHqaxr3%&sp5C%XX852#Ym@kVb";

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {


        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String header = request.getHeader("Authorization");
        validateHeader(header);
        String token = header.substring(7);

        try {
            Jwts.parser()
                    .setSigningKey(KEY)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (SignatureException | ExpiredJwtException |
                MalformedJwtException | PrematureJwtException |
                UnsupportedJwtException | IllegalArgumentException e) {

            ((HttpServletResponse) servletResponse).sendError(
                    HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
            return;
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }

    public void validateHeader(String header) throws ServletException {
        if (header == null || !header.startsWith("Bearer ")) {
            throw new ServletException("MISSING OR BADLY FORMED TOKEN");
        }
    }
}
