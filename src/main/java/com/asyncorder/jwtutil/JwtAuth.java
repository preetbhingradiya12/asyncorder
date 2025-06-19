package com.asyncorder.jwtutil;

import com.asyncorder.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.UUID;

@Component
public class JwtAuth {

    // Use proper 256-bit base64 encoded keys (generate new ones!)
    private final String secret = "5be9e237-64f0-407f-9c4f-ceafe31e8336"; // 32-byte base64
    private final String refreshSecret = "5d4ea827-d6dc-4204-b999-4179892ded7c";

    private SecretKey getSigningKey() {
//        byte[] keyBytes = Decoders.BASE64.decode(secret);
        byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private SecretKey getRefreshSigningKey() {
//        byte[] keyBytes = Decoders.BASE64.decode(refreshSecret);
        byte[] keyBytes = refreshSecret.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(User user, UUID jti) {
        long expirationMillis = 24 * 60 * 60 * 1000L;  // 1 day
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expirationMillis);

        return Jwts.builder()
                .setSubject(user.getId().toString())
                .setId(jti.toString())
                .claim("role", user.getRole().name())
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateRefreshToken(User user, UUID jti) {
        long expirationMillis = 14 * 24 * 60 * 60 * 1000L;  // 14 days
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expirationMillis);

        return Jwts.builder()
                .setSubject(user.getId().toString())
                .setId(jti.toString())
                .claim("role", user.getRole().name())
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(getRefreshSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public Claims parseToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();

//        System.out.println("Subject (userId): " + claims.getSubject());
//        System.out.println("JTI: " + claims.getId());
//        System.out.println("Role: " + claims.get("role"));
//        System.out.println("IssuedAt: " + claims.getIssuedAt());
//        System.out.println("Expiration: " + claims.getExpiration());

        return claims;
    }
}