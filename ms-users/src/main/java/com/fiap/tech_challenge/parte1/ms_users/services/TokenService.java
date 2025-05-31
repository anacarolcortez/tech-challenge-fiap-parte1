package com.fiap.tech_challenge.parte1.ms_users.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.fiap.tech_challenge.parte1.ms_users.exceptions.TokenGenerationException;
import com.fiap.tech_challenge.parte1.ms_users.exceptions.TokenValidationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

/**
 * Service responsible for generating and validating JWT tokens.
 */
@Service
public class TokenService {

    @Value("${api.security.token.secret}")
    private String secret;

    @Value("${api.security.token.issuer}")
    private String issuer;

    @Value("${api.security.token.expiration-hours}")
    private int expirationHours;

    /**
     * Generates a signed JWT token for the provided user login.
     *
     * @param userLogin the user's login (e.g. email or username)
     * @return a signed JWT token
     * @throws TokenGenerationException if any error occurs during token creation
     */
    public String generateToken(String userLogin) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.create()
                    .withIssuer(issuer)
                    .withSubject(userLogin)
                    .withExpiresAt(expirationDate())
                    .sign(algorithm);
        } catch (JWTCreationException exception) {
            throw new TokenGenerationException("Error generating JWT token", exception);
        }
    }

    /**
     * Extracts the user login (subject) from a given JWT token.
     *
     * @param tokenJWT the JWT token to verify
     * @return the subject (user login) extracted from the token
     * @throws TokenValidationException if the token is invalid or expired
     */
    public String extractUserLoginFromToken(String tokenJWT) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.require(algorithm)
                    .withIssuer(issuer)
                    .build()
                    .verify(tokenJWT)
                    .getSubject();
        } catch (JWTVerificationException exception) {
            throw new TokenValidationException("Invalid or expired JWT token");
        }
    }

    /**
     * Calculates the expiration timestamp for a new token.
     *
     * @return the expiration time as an Instant
     */
    private Instant expirationDate() {
        return ZonedDateTime.now(ZoneId.of("America/Sao_Paulo"))
                .plusHours(expirationHours)
                .toInstant();
    }

}
