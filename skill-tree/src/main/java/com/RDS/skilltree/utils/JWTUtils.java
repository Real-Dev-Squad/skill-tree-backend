package com.RDS.skilltree.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import jakarta.annotation.PostConstruct;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.stereotype.Component;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Date;
import java.util.function.Function;

@Component
@Slf4j
public class JWTUtils {

    @Value("${jwt.rds.public.key}")
    private String publicRDSKeyString;
    private KeyFactory keyFactory;

    @PostConstruct
    public void init() throws NoSuchAlgorithmException {
        keyFactory = KeyFactory.getInstance("RSA");
    }

    /**
     * Converts the given public key string to an RSAPublicKey object.
     *
     * @param publicKeyString the public key string to be converted
     * @return the RSAPublicKey object converted from the public key string
     */
    private RSAPublicKey convertToRSAPublicKey(String publicKeyString) {
        try {
            publicKeyString = publicKeyString.replace("-----BEGIN PUBLIC KEY-----", "")
                    .replace("-----END PUBLIC KEY-----", "")
                    .replaceAll("\\s", "");

            byte[] publicKeyBytes = Base64.getDecoder().decode(publicKeyString);
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKeyBytes);
            return (RSAPublicKey) keyFactory.generatePublic(keySpec);
        } catch (InvalidKeySpecException exception) {
            throw new RuntimeException("Invalid RSA key");
        }
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parser()
                .setSigningKey(convertToRSAPublicKey(publicRDSKeyString))
                .parseClaimsJws(token)
                .getBody();
    }

    private Boolean isTokenExpired(String token) {
        Claims claims = extractAllClaims(token);

        final Date expiration = claims.get("exp", Date.class);
        return expiration.before(new Date());
    }

    public String getRDSUserId(String token) {
        Claims claims = extractAllClaims(token);
        return claims.get("userId", String.class);
    }

    public String getUserRole(String token) {
        Claims claims = extractAllClaims(token);

        return claims.get("role", String.class);
    }

    public boolean validateToken(String token) {

        return (!isTokenExpired(token));
    }

}
