package vn.trivd.hospitalgateway.auth.controller;

import com.example.hospitalenities.util.JSONFactory;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @Autowired
    private EmailService emailService;

    @Autowired
    private AuthService authService;

    @Autowired
    private JwtSecretProvider jwtSecretProvider;

    @PostMapping("/register/request-otp")
    public ResponseEntity<?> requestOtp(@Valid @RequestBody RegisterRequestOtpReq request, HttpServletRequest httpServletRequest) {
        log.info("[register request-otp] Request OTP for {}", request.getEmail());
        ResponseObject responseObject = authService.requestOtp(request,httpServletRequest);
        log.info("[register request-otp] Response OTP for {}", JSONFactory.toJson(responseObject));
        return new ResponseEntity<>(responseObject, HttpStatus.OK);
    }


    @PostMapping("/register/verify-otp")
    public ResponseEntity<?> verifyOtp(@Valid @RequestBody RegisterVerifyOtpReq req, HttpServletRequest httpServletRequest) {
        log.info("[register verify-otp] Request OTP for {}", req.getEmail());
        ResponseObject responseObject = authService.generateregistrationToken(req,httpServletRequest);
        log.info("[register verify-otp] Response OTP for {}", req.getEmail());
        return new ResponseEntity<>(responseObject, HttpStatus.OK);
    }

    @PostMapping("/register/complete")
    public ResponseEntity<?> complete(@Valid @RequestBody RegisterCompleteReq req) {
        authService.completeRegistration(req);
        /*emailService.sendAccountCreated(req.getEmail(), req.getEmail());*/
        return null;
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

