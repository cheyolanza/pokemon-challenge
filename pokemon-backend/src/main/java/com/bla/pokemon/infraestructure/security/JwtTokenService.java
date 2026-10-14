
package com.bla.pokemon.infraestructure.security;

import com.bla.pokemon.application.port.TokenService;
import com.bla.pokemon.domain.model.User;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Component
public class JwtTokenService implements TokenService {

    private final JwtEncoder encoder;
    private final long expirationMinutes;

    public JwtTokenService(
            JwtEncoder encoder,
            @Value("${app.jwt.expiration-minutes:60}")
            long expirationMinutes
    ) {
        this.encoder = encoder;
        this.expirationMinutes = expirationMinutes;
    }

    @Override
    public String generate(User user) {
        Instant now = Instant.now();

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("pokemon-backend")
                .subject(user.id().toString())
                .issuedAt(now)
                .expiresAt(now.plus(
                        expirationMinutes,
                        ChronoUnit.MINUTES
                ))
                .claim("email", user.email())
                .claim("username", user.username())
                .claim("roles", List.of(user.role()))
                .build();

        JwsHeader header = JwsHeader.with(
                MacAlgorithm.HS256
        ).build();

        return encoder.encode(
                JwtEncoderParameters.from(header, claims)
        ).getTokenValue();
    }
}
