package org.example.app.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.example.app.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;
import java.util.Map;

@Service
public class JwtService {
    private final String SECRET_KEY;
    private final UserRepository userRepository;

    public JwtService(UserRepository userRepository) {
        this.userRepository = userRepository;
        try {
            KeyGenerator keyGen = KeyGenerator.getInstance("HmacSHA256");
            SecretKey sk = keyGen.generateKey();
            SECRET_KEY = Base64.getEncoder().encodeToString(sk.getEncoded());

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }


    public String generateToken(String email) {
        final Date now = new Date();

        return Jwts.builder()
                .subject(email)
                .claims(Map.of())
                .issuedAt(now)
                .notBefore(now)
                .expiration(new Date(now.getTime() + 1000 * 60 * 60))
                .signWith(getSignInKey(), Jwts.SIG.HS256)
                .compact();
    }

    public UserDetails getUserDetails(String token) {
        final Claims claims = getClaims(token);
        final String email = claims.getSubject();

        return this.userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found!"));
    }

    private Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private SecretKey getSignInKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }
}
