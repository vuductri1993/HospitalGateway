package vn.trivd.hospitalgateway.authz;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;

public class JwtUtil {
    public static Jws<Claims> verifyToken(String token, String secret) {
        try {
            SecretKey key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
            return Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token);
        } catch (io.jsonwebtoken.security.SignatureException e) {
            throw new RuntimeException("Invalid JWT Signature");
        } catch (MalformedJwtException e) {
            throw new RuntimeException("Invalid JWT Token");
        } catch (ExpiredJwtException e) {
            throw new RuntimeException("Expired JWT Token");
        } catch (UnsupportedJwtException e) {
            throw new RuntimeException("Unsupported JWT Token");
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("JWT Claims string is empty.");
        }
    }
}

