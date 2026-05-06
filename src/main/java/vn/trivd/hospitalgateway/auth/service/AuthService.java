package vn.trivd.hospitalgateway.auth.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.hospitalenities.entity.Doctor;
import com.example.hospitalenities.entity.Patient;
import com.example.hospitalenities.entity.Role;
import vn.trivd.hospitalgateway.auth.dto.LoginRes;
import vn.trivd.hospitalgateway.auth.dto.RegisterCompleteReq;
import vn.trivd.hospitalgateway.auth.dto.RegisterRequestOtpReq;
import vn.trivd.hospitalgateway.authz.Constants;
import vn.trivd.hospitalgateway.repository.DoctorRepository;
import vn.trivd.hospitalgateway.repository.PatientRepository;
import vn.trivd.hospitalgateway.repository.RoleRepository;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

@Service
@Slf4j
public class AuthService {

    private final PasswordEncoder passwordEncoder;
    private final JwtIssuer jwtIssuer;
    private final StringRedisTemplate redis;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
    private final RoleRepository roleRepository;
    @Autowired
    private OtpService otpService;

    @Autowired
    private EmailService emailService;

    public AuthService(
            PasswordEncoder passwordEncoder,
            JwtIssuer jwtIssuer,
            StringRedisTemplate redis,
            DoctorRepository doctorRepository,
            PatientRepository patientRepository,
            RoleRepository roleRepository
    ) {
        this.passwordEncoder = passwordEncoder;
        this.jwtIssuer = jwtIssuer;
        this.redis = redis;
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
        this.roleRepository = roleRepository;
    }

    @Transactional
    public Long completeRegistration(RegisterCompleteReq req) {
        String userType = normalizeUserType(req.getUserType());
        String email = req.getEmail().toLowerCase(Locale.ROOT);

        // Ensure base role exists (role_code DOCTOR/PATIENT)
        Role role = roleRepository.findByRoleCode(userType).orElseGet(() -> {
            Role r = new Role();
            r.setRoleCode(userType);
            r.setRoleName(userType);
            r.setActive(true);
            return roleRepository.save(r);
        });

        if (Constants.HOSPITAL_DOCTOR.equals(userType)) {
            Doctor d = new Doctor();
            d.setRole(role);
            d.setFullName(req.getFullName());
            d.setEmail(email);
            d.setPhone(null);
            d.setSpecialty(null);
            d.setLicenseNumber(null);
            d = doctorRepository.save(d);

            // Store password hash in Redis (no custom auth entity/table)
            redis.opsForValue().set(credKeyDoctor(d.getDoctorId().intValue()), passwordEncoder.encode(req.getPassword()));
            return d.getDoctorId();
        } else {
            Patient p = new Patient();
            p.setRole(role);
            p.setFullName(req.getFullName());
            // patient requires date_of_birth (nullable=false) in shared entity
            p.setDateOfBirth(LocalDate.of(1970, 1, 1));
            p.setGender(null);
            p.setNationalId(null);
            p.setPhone(null);
            p.setAddress(null);
            p.setBloodType(null);
            p = patientRepository.save(p);

            redis.opsForValue().set(credKeyPatient(p.getPatientId().intValue()), passwordEncoder.encode(req.getPassword()));
            return p.getPatientId();
        }
    }

    public LoginRes login(String userTypeRaw, String emailRaw, String rawPassword) {
        String userType = normalizeUserType(userTypeRaw);
        String email = emailRaw.toLowerCase(Locale.ROOT);

        String token;
        String tokenSetKey;
        String permKey;

        Long id;
        String fullName;
        String title = null;
        Long departmentId = null;
        boolean departmentHead = false;

        if (Constants.HOSPITAL_DOCTOR.equals(userType)) {
            // shared Doctor entity has email field, but repository doesn't expose findByEmail -> use simple scan is not ideal.
            // For now: login by doctorId in email field is unsupported; you should add a core service for auth, or extend repository in shared module.
            throw new UnsupportedOperationException("Doctor login by email is not supported by current shared repositories. Please add DoctorRepository.findByEmail in Hospital-Enities.");
        } else {
            throw new UnsupportedOperationException("Patient login by email is not supported by shared Patient entity (no email). Please login by phone or add email to Patient entity.");
        }

        // unreachable
    }

    public void logoutDoctor(int doctorId, String token) {
        if (token == null || token.isBlank()) return;
        redis.opsForSet().remove(Constants.HOSPITAL_DOCTOR_TOKEN_ + doctorId, token);
    }

    public void logoutPatient(int patientId, String token) {
        if (token == null || token.isBlank()) return;
        redis.opsForSet().remove(Constants.HOSPITAL_PATIENT_TOKEN_ + patientId, token);
    }

    public void requestOtp(RegisterRequestOtpReq req) {
        String otp = otpService.generateOTP(6);
        otpService.storeRegisterOtp(req.getEmail(), otp);
        emailService.sendOtp(req.getEmail(), otp);
    }

    public boolean verifyOtp(String email, String otp) {
        return otpService.verifyRegisterOtp(email, otp);
    }

    private static String normalizeUserType(String raw) {
        if (raw == null) throw new IllegalArgumentException("userType is required");
        String v = raw.trim().toUpperCase(Locale.ROOT);
        if (!Constants.HOSPITAL_DOCTOR.equals(v) && !Constants.HOSPITAL_PATIENT.equals(v)) {
            throw new IllegalArgumentException("userType must be DOCTOR or PATIENT");
        }
        return v;
    }

    private static String credKeyDoctor(int doctorId) {
        return "HOSPITAL_DOCTOR_CRED_" + doctorId;
    }

    private static String credKeyPatient(int patientId) {
        return "HOSPITAL_PATIENT_CRED_" + patientId;
    }
}

