package vn.trivd.hospitalgateway.authz;

import java.util.List;

public class Constants {
    public static final String TOKEN_PREFIX = "Bearer ";

    // Match backend JwtAuthFilter contract
    public static final String HOSPITAL_DOCTOR = "DOCTOR";
    public static final String HOSPITAL_PATIENT = "PATIENT";

    // Whitelist token sets
    public static final String HOSPITAL_DOCTOR_TOKEN_ = "HOSPITAL_DOCTOR_TOKEN_";
    public static final String HOSPITAL_PATIENT_TOKEN_ = "HOSPITAL_PATIENT_TOKEN_";

    // Permission caches (string "P1:P2")
    public static final String REDIS_HOSPITAL_USER_PERMISSION_DOCTOR_ = "REDIS_HOSPITAL_USER_PERMISSION_DOCTOR_";
    public static final String REDIS_HOSPITAL_USER_PERMISSION_PATIENT_ = "REDIS_HOSPITAL_USER_PERMISSION_PATIENT_";

    public static final String SIGN_PIPE = ":";

    public static final List<String> API_PUBLIC = List.of(
            "/auth/login",
            "/auth/logout",
            "/auth/register/request-otp",
            "/auth/register/verify-otp",
            "/auth/register/complete"
    );
}

