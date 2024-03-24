package com.RDS.skilltree.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.annotation.PostConstruct;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class JWTUtils {

    @Value("${jwt.rds.public.key}")
    private String publicRDSKeyString;

    /* the RSAPublicKey object converted from the public key string*/
    private RSAPublicKey publicKey;

    /* Converts the given public key string to an RSAPublicKey object. */
    @PostConstruct
    public void init() throws NoSuchAlgorithmException, InvalidKeySpecException {
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        publicRDSKeyString =
                publicRDSKeyString
                        .replace("-----BEGIN PUBLIC KEY-----", "")
                        .replace("-----END PUBLIC KEY-----", "")
                        .replaceAll("\\s", "");

        byte[] publicKeyBytes = Base64.getDecoder().decode(publicRDSKeyString);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKeyBytes);
        publicKey = (RSAPublicKey) keyFactory.generatePublic(keySpec);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(publicKey).parseClaimsJws(token).getBody();
    }

    public String getRDSUserId(Claims claims) {
        return claims.get("userId", String.class);
    }

    public String getUserRole(Claims claims) {
        return claims.get("role", String.class);
    }

    public Claims validateToken(String token) {
        Claims claims = extractAllClaims(token);
        return claims;
    }
}
