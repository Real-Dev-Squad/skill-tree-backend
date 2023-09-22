package com.RDS.skilltree.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
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

@Component
public class JWTUtils {

    @Value("${jwt.rds.public.key}")
    private String publicRDSKeyString;

    /**
     * Converts the given private key string to an RSAPrivateKey object.
     *
     * @param privateKeyString The private key string to be converted.
     * @return The corresponding RSAPrivateKey object.
     * @throws Exception If an error occurs during the conversion process.
     */
    private RSAPrivateKey convertToRSAPrivateKey(String privateKeyString) throws Exception {
        try {
            privateKeyString = privateKeyString.replace("-----BEGIN PRIVATE KEY-----", "")
                    .replace("-----END PRIVATE KEY-----", "")
                    .replaceAll("\\s", "");

            byte[] privateKeyBytes = Base64.getDecoder().decode(privateKeyString);
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException | IllegalArgumentException e) {
            throw new Exception("Error converting private key: " + e.getMessage(), e);
        }
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
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return (RSAPublicKey) keyFactory.generatePublic(keySpec);
    }

    public String getRDSUserId(String token) throws Exception {
        Claims claims = Jwts.parser().setSigningKey(convertToRSAPublicKey(publicRDSKeyString)).parseClaimsJws(token).getBody();
        String temp = claims.get("userId", String.class);
        return temp;
    }

    public boolean validateToken(String token) throws Exception {
        try {
            Jwts.parser().setSigningKey(convertToRSAPublicKey(publicRDSKeyString)).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            throw new AuthenticationCredentialsNotFoundException("Invalid JWT");
        }
    }

}
