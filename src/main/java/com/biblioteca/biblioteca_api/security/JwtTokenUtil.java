package com.biblioteca.biblioteca_api.security;

import com.biblioteca.biblioteca_api.model.Role;
import com.biblioteca.biblioteca_api.model.User;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.*;
import com.nimbusds.jwt.*;

import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class JwtTokenUtil {

    private static final String SECRET = "secret-key-12345678901234567890123456789012";
    private static final JWSAlgorithm ALGORITHM = JWSAlgorithm.HS256;

    public String generateToken(User user) {
        try {
            JWSSigner signer = new MACSigner(SECRET);

            List<String> roles = user.getRoles().stream()
                .map(Role::getRole)
                .collect(Collectors.toList());

            JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .subject(user.getEmail())
                .claim("roles", roles)
                .expirationTime(new Date(new Date().getTime() + 86400000)) //un dia, esta en segundos.
                .build();

            SignedJWT signedJWT = new SignedJWT(
                new JWSHeader(ALGORITHM),
                claimsSet
            );

            signedJWT.sign(signer);
            return signedJWT.serialize();

        } catch (Exception e) {
            throw new RuntimeException("Error generating JWT", e);
        }
    }

    public JWTClaimsSet validateToken(String token) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            JWSVerifier verifier = new MACVerifier(SECRET);
            if (!signedJWT.verify(verifier)) {
                throw new RuntimeException("Token signature is invalid");
            }

            JWTClaimsSet claims = signedJWT.getJWTClaimsSet();
            if (claims.getExpirationTime().before(new Date())) {
                throw new RuntimeException("Token expired");
            }

            return claims;
        } catch (Exception e) {
            throw new RuntimeException("Invalid token", e);
        }
    }
}