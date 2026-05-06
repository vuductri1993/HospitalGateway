package vn.trivd.hospitalgateway.authz;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtSecretProvider {
    private final String secret;

    public JwtSecretProvider(@Value("${app.security.jwt.secret:}") String secretFromConfig) {
        String fromEnv = System.getenv("JWT_SECRET");
        String chosen = StringUtils.isNotBlank(secretFromConfig) ? secretFromConfig : fromEnv;
        if (StringUtils.isBlank(chosen)) {
            throw new IllegalStateException("JWT secret is missing. Set env JWT_SECRET or app.security.jwt.secret");
        }
        this.secret = chosen;
    }

    public String secret() {
        return secret;
    }
}

