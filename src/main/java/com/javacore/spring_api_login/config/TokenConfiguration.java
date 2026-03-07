package com.javacore.spring_api_login.config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.javacore.spring_api_login.entity.User;
import com.javacore.spring_api_login.entity.UserRole;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Component
public class TokenConfiguration {

    @Value("${jwt.secret}")
    private String secret;

    public String generateToken(User user) {
        Algorithm algorithm = Algorithm.HMAC256(secret);

        return JWT.create()
                .withSubject(user.getEmail())
                .withClaim("publicId", user.getPublicId().toString())
                .withClaim("role", user.getRole().name())
                .withIssuedAt(new Date())
                .withExpiresAt(Date.from(Instant.now().plus(2, ChronoUnit.HOURS)))
                .sign(algorithm);
    }

    public Optional<JWTUserData> validateToken(String token) {
        try {
            var decoded = JWT.require(Algorithm.HMAC256(secret))
                    .build()
                    .verify(token);

            return Optional.of(JWTUserData.builder()
                    .email(decoded.getSubject())
                    .publicId(UUID.fromString(decoded.getClaim("publicId").asString()))
                    .role(UserRole.valueOf(decoded.getClaim("role").asString()))
                    .build());
        } catch (JWTVerificationException ex) {
            return Optional.empty();
        }
    }
}
