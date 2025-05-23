package io.github.ZalaHarshpalsinh.peer_to_peer_lending_platform.services;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.stream.Collectors;

// service for token
@Service
public class TokenService
{
    private final JwtEncoder jwtEncoder;

    // DI
    public TokenService(JwtEncoder jwtEncoder){
        this.jwtEncoder = jwtEncoder;
    }

    public String generateToken(Authentication authentication)
    {
        String scope = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "));
        return encode(authentication.getName(), scope);
    }

    //method to encode
    public String encode(String subject, String scope)
    {
        Instant now = Instant.now();
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now)
                .expiresAt(now.plus(10, ChronoUnit.HOURS))
                .subject(subject)
                .claim("scope", scope)
                .build();

        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }
}
