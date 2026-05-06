package vn.trivd.hospitalgateway.authz;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class BaseServiceUtil {
    private final StringRedisTemplate redis;

    public BaseServiceUtil(StringRedisTemplate redis) {
        this.redis = redis;
    }

    public boolean isValidDoctorToken(Integer doctorId, String token) {
        if (Objects.isNull(doctorId) || StringUtils.isBlank(token)) {
            return false;
        }
        Boolean isValid = redis.opsForSet().isMember(Constants.HOSPITAL_DOCTOR_TOKEN_ + doctorId, token);
        return Objects.nonNull(isValid) && isValid;
    }

    public boolean isValidPatientToken(Integer patientId, String token) {
        if (Objects.isNull(patientId) || StringUtils.isBlank(token)) {
            return false;
        }
        Boolean isValid = redis.opsForSet().isMember(Constants.HOSPITAL_PATIENT_TOKEN_ + patientId, token);
        return Objects.nonNull(isValid) && isValid;
    }
}

