package com.mypractice.estudy.auth.security.provider;

import com.mypractice.estudy.auth.exception.GenericException;
import com.mypractice.estudy.auth.security.oauth2.user.UserPrincipal;
import com.mypractice.estudy.auth.util.ApplicationProperties;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

@Service
public record JwtTokenProvider(ApplicationProperties applicationProperties) {


    public String createToken(final Authentication authentication) {
        final var userPrincipal = (UserPrincipal) authentication.getPrincipal();
        var now = new Date();
        var expiryDate = new Date(now.getTime() + applicationProperties.getAuthDto().getTokenExpiration());
        return Jwts.builder()
                .setSubject(userPrincipal.getId())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(expiryDate)
                .signWith(getSigningKey())
                .compact();
    }

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(applicationProperties.getAuthDto().getTokenSecret()));
    }

    public boolean validateToken(final String authToken) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(authToken);
            return true;
        } catch (MalformedJwtException ex) {
            throw new GenericException("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            throw new GenericException("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            throw new GenericException("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            throw new GenericException("Jwt claim is  invalid token");
        }
    }


    public String getUserIdFromToken(final String token) {
        var claims = Jwts.parserBuilder()
                .setSigningKey(getSigningKey()).build()
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

}
