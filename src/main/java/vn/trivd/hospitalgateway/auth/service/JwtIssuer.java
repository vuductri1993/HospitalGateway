package vn.trivd.hospitalgateway.auth.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import vn.trivd.hospitalgateway.authz.JwtSecretProvider;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;

@Service
public class JwtIssuer {
    private final SecretKey key;
    private final String issuer;
    private final long accessTtlSeconds;
    private final long registerTtlSeconds;

    public JwtIssuer(
            JwtSecretProvider jwtSecretProvider,
            @Value("${app.security.jwt.issuer:hospital-gateway}") String issuer,
            @Value("${app.security.jwt.access-ttl-seconds:3600}") long accessTtlSeconds,
            @Value("${app.security.jwt.register-ttl-seconds:600}") long registerTtlSeconds
    ) {
        this.key = Keys.hmacShaKeyFor(jwtSecretProvider.secret().getBytes(StandardCharsets.UTF_8));
        this.issuer = issuer;
        this.accessTtlSeconds = accessTtlSeconds;
        this.registerTtlSeconds = registerTtlSeconds;
    }

    public long accessTtlSeconds() {
        return accessTtlSeconds;
    }

    public String issueRegisterToken(String email) {
        Instant now = Instant.now();
        return Jwts.builder()
                .issuer(issuer)
                .subject(email.toLowerCase())
                .claim("typ", "REG")
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plusSeconds(registerTtlSeconds)))
                .signWith(key)
                .compact();
    }

    public String issueAccessTokenDoctor(int doctorId, String email) {
        Instant now = Instant.now();
        return Jwts.builder()
                .issuer(issuer)
                .subject(vn.trivd.hospitalgateway.authz.Constants.HOSPITAL_DOCTOR)
                .claim("doctorId", doctorId)
                .claim("email", email)
                .claim("typ", "ACCESS")
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plusSeconds(accessTtlSeconds)))
                .signWith(key)
                .compact();
    }

    public String issueAccessTokenPatient(int patientId, String email) {
        Instant now = Instant.now();
        return Jwts.builder()
                .issuer(issuer)
                .subject(vn.trivd.hospitalgateway.authz.Constants.HOSPITAL_PATIENT)
                .claim("patientId", patientId)
                .claim("email", email)
                .claim("typ", "ACCESS")
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plusSeconds(accessTtlSeconds)))
                .signWith(key)
                .compact();
    }
}

