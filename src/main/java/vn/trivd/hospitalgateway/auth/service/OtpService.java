package vn.trivd.hospitalgateway.auth.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.time.Duration;
import java.util.HexFormat;
import java.util.Random;

@Service
public class OtpService {
    private final StringRedisTemplate redis;
    private final int ttlSeconds;
    private final int maxAttempts;
    private final Random random = new Random();

    public OtpService(
            StringRedisTemplate redis,
            @Value("${app.otp.ttl-seconds:300}") int ttlSeconds,
            @Value("${app.otp.max-attempts:5}") int maxAttempts
    ) {
        this.redis = redis;
        this.ttlSeconds = ttlSeconds;
        this.maxAttempts = maxAttempts;
    }

    private static final SecureRandom secureRandom = new SecureRandom();
    private static final String NUMBERS = "0123456789";

    public String generateOTP(int length) {
        StringBuilder otp = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            otp.append(NUMBERS.charAt(secureRandom.nextInt(NUMBERS.length())));
        }

        return otp.toString();
    }

    public void storeRegisterOtp(String email, String otp) {
        String key = otpKey(email);
        redis.opsForValue().set(key, otpHash(otp) + "|0", Duration.ofSeconds(ttlSeconds));
    }

    public boolean verifyRegisterOtp(String email, String otp) {
        String key = otpKey(email);
        String val = redis.opsForValue().get(key);
        if (val == null) return false;

        String[] parts = val.split("\\|");
        String storedHash = parts[0];
        int attempts = parts.length > 1 ? Integer.parseInt(parts[1]) : 0;
        if (attempts >= maxAttempts) return false;

        boolean ok = storedHash.equals(otpHash(otp));
        if (ok) {
            redis.delete(key);
            return true;
        }

        attempts++;
        redis.opsForValue().set(key, storedHash + "|" + attempts, Duration.ofSeconds(ttlSeconds));
        return false;
    }

    private String otpKey(String email) {
        return "otp:register:" + email.toLowerCase();
    }

    private String otpHash(String otp) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] dig = md.digest(otp.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(dig);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }
}

