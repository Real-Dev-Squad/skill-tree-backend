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
     * @throws Exception if there is an error during the conversion process
     */
    private RSAPublicKey convertToRSAPublicKey(String publicKeyString) throws Exception {
        publicKeyString = publicKeyString.replace("-----BEGIN PUBLIC KEY-----", "")
                .replace("-----END PUBLIC KEY-----", "")
                .replaceAll("\\s", "");

        byte[] publicKeyBytes = Base64.getDecoder().decode(publicKeyString);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKeyBytes);
        return (RSAPublicKey) keyFactory.generatePublic(keySpec);
    }

    private Claims extractAllClaims (String token) throws Exception{
        return Jwts
                .parser()
                .setSigningKey(convertToRSAPublicKey(publicRDSKeyString))
                .parseClaimsJws(token)
                .getBody();
    }

    private Boolean isTokenExpired(String token) throws Exception {
        Claims claims = extractAllClaims(token);

        final Date expiration = claims.get("exp", Date.class);
        return expiration.before(new Date());
    }

    public String getRDSUserId(String token) throws Exception {
        Claims claims = extractAllClaims(token);
        String rdsUserId = claims.get("userId", String.class);
        return rdsUserId;
    }
    public String getUserRole(String token) throws Exception {
        Claims claims = extractAllClaims(token);

        String role = claims.get("role", String.class);
        return role;
    }

    public boolean validateToken(String token) throws Exception { //TODO check for the case where token is expired
        try {
            return (!isTokenExpired(token));

        } catch (Exception e) {
            throw new AuthenticationCredentialsNotFoundException("Invalid JWT");
        }
    }

}