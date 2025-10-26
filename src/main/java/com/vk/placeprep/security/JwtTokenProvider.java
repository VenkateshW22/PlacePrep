package com.vk.placeprep.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import javax.crypto.SecretKey;
import java.util.Date;

/**
 * Component responsible for JWT token generation, validation, and parsing.
 * Handles all JWT-related operations including token creation, validation,
 * and extraction of user information from tokens.
 * 
 * @author Venkatesh K
 * @version 1.0
 * @since 2025-10-26
 */
@Component
public class JwtTokenProvider {
    private static final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);

    /**
     * Secret key used for signing and verifying JWT tokens.
     * Injected from application properties.
     */
    @Value("${app.jwtSecret}")
    private String jwtSecret;

    /**
     * Expiration time for JWT tokens in milliseconds.
     * Injected from application properties.
     */
    @Value("${app.jwtExpirationInMs}")
    private int jwtExpirationInMs;

    /**
     * Generates a secret key for signing JWT tokens.
     * 
     * @return A SecretKey instance for JWT signing
     */
    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    /**
     * Generates a JWT token for the authenticated user.
     *
     * @param authentication The authentication object containing the user principal
     * @return A JWT token as a String
     * @throws IllegalArgumentException if the authentication principal is not a UserPrincipal
     */
    public String generateToken(Authentication authentication) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationInMs);

        return Jwts.builder()
                .setSubject(Long.toString(userPrincipal.getId()))
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .signWith(getSigningKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    /**
     * Extracts the user ID from a JWT token.
     *
     * @param token The JWT token to parse
     * @return The user ID contained in the token
     * @throws ExpiredJwtException if the token is expired
     * @throws UnsupportedJwtException if the token is unsupported
     * @throws MalformedJwtException if the token is malformed
     * @throws SignatureException if the token's signature is invalid
     * @throws IllegalArgumentException if the token is null or empty
     */
    public Long getUserIdFromJWT(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
        return Long.parseLong(claims.getSubject());
    }

    /**
     * Validates a JWT token.
     *
     * @param authToken The JWT token to validate
     * @return true if the token is valid, false otherwise
     */
    public boolean validateToken(String authToken) {
        try {
            Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(authToken);
            return true;
        } catch (SecurityException ex) {
            logger.error("Invalid JWT signature");
        } catch (MalformedJwtException ex) {
            logger.error("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            logger.error("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            logger.error("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            logger.error("JWT claims string is empty.");
        }
        return false;
    }
}
