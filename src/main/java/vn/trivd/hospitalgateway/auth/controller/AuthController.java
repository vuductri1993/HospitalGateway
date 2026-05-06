package vn.trivd.hospitalgateway.auth.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;
import vn.trivd.hospitalgateway.auth.dto.*;
import vn.trivd.hospitalgateway.auth.service.*;
import vn.trivd.hospitalgateway.authz.Constants;
import vn.trivd.hospitalgateway.authz.JwtSecretProvider;
import vn.trivd.hospitalgateway.authz.JwtUtil;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@Slf4j
public class AuthController {
    private final OtpService otpService;
    private final EmailService emailService;
    private final JwtIssuer jwtIssuer;
    private final AuthService authService;

    private final JwtSecretProvider jwtSecretProvider;

    public AuthController(
            OtpService otpService,
            EmailService emailService,
            JwtIssuer jwtIssuer,
            AuthService authService,
            JwtSecretProvider jwtSecretProvider
    ) {
        this.otpService = otpService;
        this.emailService = emailService;
        this.jwtIssuer = jwtIssuer;
        this.authService = authService;
        this.jwtSecretProvider = jwtSecretProvider;
    }

    @PostMapping("/register/request-otp")
    public Map<String, Object> requestOtp(@Valid @RequestBody RegisterRequestOtpReq req) {
        authService.requestOtp(req);
        return Map.of("sent", true);
    }


    @PostMapping("/register/verify-otp")
    public Map<String, Object> verifyOtp(@Valid @RequestBody RegisterVerifyOtpReq req) {
        boolean ok = authService.verifyOtp(req.getEmail(), req.getOtp());
        if (!ok) return Map.of("verified", false);
        String registrationToken = jwtIssuer.issueRegisterToken(req.getEmail());
        return Map.of("verified", true, "registrationToken", registrationToken);
    }

    @PostMapping("/register/complete")
    public Map<String, Object> complete(@Valid @RequestBody RegisterCompleteReq req) {
        // registerToken chỉ dùng nội bộ gateway, không ảnh hưởng backend JwtAuthFilter
        // (nếu bạn muốn enforce thêm thì mình sẽ parse typ=REG tương tự trước)
        Long userId = authService.completeRegistration(req);
        emailService.sendAccountCreated(req.getEmail(), req.getEmail());
        return Map.of("created", true, "userId", userId);
    }

    @PostMapping("/login")
    public LoginRes login(@Valid @RequestBody LoginReq req) {
        return authService.login(req.getUserType(), req.getEmail(), req.getPassword());
    }

    @PostMapping("/logout")
    public Map<String, Object> logout(@RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authHeader) {
        if (StringUtils.isBlank(authHeader) || !authHeader.startsWith(Constants.TOKEN_PREFIX)) {
            return Map.of("success", true);
        }

        String token = authHeader.replace(Constants.TOKEN_PREFIX, "").trim();
        try {
            var claims = JwtUtil.verifyToken(token, jwtSecretProvider.secret()).getBody();
            String subject = claims.getSubject();
            if (Constants.HOSPITAL_DOCTOR.equals(subject)) {
                Integer doctorId = claims.get("doctorId", Integer.class);
                if (doctorId != null) authService.logoutDoctor(doctorId, token);
            } else if (Constants.HOSPITAL_PATIENT.equals(subject)) {
                Integer patientId = claims.get("patientId", Integer.class);
                if (patientId != null) authService.logoutPatient(patientId, token);
            }
        } catch (Exception e) {
            return Map.of("success", true);
        }
        return Map.of("success", true);
    }
}

