package com.ludens.assignment.global.security;

import com.ludens.assignment.global.config.properties.JwtProperties;
import com.ludens.assignment.global.exception.AuthException;
import com.ludens.assignment.user.domain.MemberRole;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class TokenProvider {

    private static final String ROLE_CLAIM = "role";

    private final SecretKey signingKey;
    private final long expMillis;

    public TokenProvider(JwtProperties jwtProperties) {
        this.signingKey = Keys.hmacShaKeyFor(
                jwtProperties.getSecret().getBytes(StandardCharsets.UTF_8));
        this.expMillis = jwtProperties.getExpiration() * 60 * 1000L;
    }

    public String generateToken(String userId, MemberRole role) {
        Date now = new Date();
        return Jwts.builder()
                .subject(userId)
                .claim(ROLE_CLAIM, role.name())
                .issuedAt(now)
                .expiration(new Date(now.getTime() + expMillis))
                .signWith(signingKey)
                .compact();
    }

    public Claims parseClaims(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(signingKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (ExpiredJwtException e) {
            throw AuthException.expiredToken();
        } catch (JwtException | IllegalArgumentException e) {
            throw AuthException.invalidToken();
        }
    }
}
