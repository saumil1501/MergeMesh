
package com.mergemesh.backend.security;

import java.time.Instant;

import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.stereotype.Service;

import com.mergemesh.backend.auth.LoginResponse;
import com.mergemesh.backend.user.User;

@Service
public class JwtService {

    private static final long ACCESS_TOKEN_LIFETIME_SECONDS = 900;

    private final JwtEncoder jwtEncoder;

    public JwtService(JwtEncoder jwtEncoder) {
        this.jwtEncoder = jwtEncoder;
    }

    public LoginResponse createAccessToken(User user) {
        Instant issuedAt = Instant.now();
        Instant expiresAt = issuedAt.plusSeconds(
                ACCESS_TOKEN_LIFETIME_SECONDS);

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("mergemesh")
                .subject(user.getId().toString())
                .issuedAt(issuedAt)
                .expiresAt(expiresAt)
                .claim("email", user.getEmail())
                .claim("displayName", user.getDisplayName())
                .build();

        JwsHeader header = JwsHeader.with(MacAlgorithm.HS256).build();

        String token = jwtEncoder.encode(
                JwtEncoderParameters.from(header, claims))
                .getTokenValue();

        return new LoginResponse(
                token,
                "Bearer",
                expiresAt,
                user.getId(),
                user.getDisplayName(),
                user.getEmail());
    }
}
