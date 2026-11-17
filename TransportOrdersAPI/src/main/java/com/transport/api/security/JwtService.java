package com.transport.api.security;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
@Service public class JwtService {
 private final SecretKey key;
 public JwtService(@Value("${app.jwt.secret}") String secret){this.key=Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));}
 public String generate(String username){long now=System.currentTimeMillis();return Jwts.builder().subject(username).issuedAt(new Date(now)).expiration(new Date(now+3600000)).signWith(key).compact();}
 public String subject(String token){return Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload().getSubject();}
}
