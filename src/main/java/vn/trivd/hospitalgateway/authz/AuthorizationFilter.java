package vn.trivd.hospitalgateway.authz;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AuthorizationFilter extends BasicAuthenticationFilter {
    private final BaseServiceUtil baseServiceUtil;
    private final StringRedisTemplate redis;
    private final JwtSecretProvider jwtSecretProvider;

    public AuthorizationFilter(
            AuthenticationManager authenticationManager,
            BaseServiceUtil baseServiceUtil,
            StringRedisTemplate redis,
            JwtSecretProvider jwtSecretProvider
    ) {
        super(authenticationManager);
        this.baseServiceUtil = baseServiceUtil;
        this.redis = redis;
        this.jwtSecretProvider = jwtSecretProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        SecurityContext context = SecurityContextHolder.getContext();
        String header = req.getHeader(HttpHeaders.AUTHORIZATION);

        if (StringUtils.isBlank(header) || !header.startsWith(Constants.TOKEN_PREFIX)) {
            chain.doFilter(req, res);
            return;
        }

        String token = header.replace(Constants.TOKEN_PREFIX, "").trim();

        try {
            Jws<Claims> claimsJws = JwtUtil.verifyToken(token, jwtSecretProvider.secret());
            Claims claims = claimsJws.getBody();

            String subject = claims.getSubject();
            Integer principalId;
            String permissionKey;

            if (StringUtils.equals(subject, Constants.HOSPITAL_DOCTOR)) {
                principalId = claims.get("doctorId", Integer.class);
                if (principalId == null || !baseServiceUtil.isValidDoctorToken(principalId, token)) {
                    res.setStatus(HttpStatus.UNAUTHORIZED.value());
                    return;
                }
                permissionKey = Constants.REDIS_HOSPITAL_USER_PERMISSION_DOCTOR_ + principalId;
            } else if (StringUtils.equals(subject, Constants.HOSPITAL_PATIENT)) {
                principalId = claims.get("patientId", Integer.class);
                if (principalId == null || !baseServiceUtil.isValidPatientToken(principalId, token)) {
                    res.setStatus(HttpStatus.UNAUTHORIZED.value());
                    return;
                }
                permissionKey = Constants.REDIS_HOSPITAL_USER_PERMISSION_PATIENT_ + principalId;
            } else {
                writeJwtErrorResponse(res, HttpStatus.UNAUTHORIZED.value(), "Invalid token subject");
                return;
            }

            String userPermissionData = redis.opsForValue().get(permissionKey);

            List<String> permissions = new ArrayList<>();
            if (StringUtils.isNotBlank(userPermissionData)) {
                for (String p : userPermissionData.split(Constants.SIGN_PIPE)) {
                    if (StringUtils.isNotBlank(p)) permissions.add(p.trim());
                }
            }

            List<GrantedAuthority> authorities = permissions.stream()
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());

            var authentication = new UsernamePasswordAuthenticationToken(principalId, null, authorities);
            context.setAuthentication(authentication);
            chain.doFilter(req, res);
        } catch (ExpiredJwtException ex) {
            writeJwtErrorResponse(res, HttpStatus.UNAUTHORIZED.value(), "JWT token has expired");
        } catch (JwtException | IllegalArgumentException ex) {
            writeJwtErrorResponse(res, HttpStatus.UNAUTHORIZED.value(), "Invalid JWT token");
        } catch (Exception ex) {
            writeJwtErrorResponse(res, HttpStatus.UNAUTHORIZED.value(), ex.getMessage());
        }
    }

    private void writeJwtErrorResponse(HttpServletResponse res, int status, String message) throws IOException {
        res.setStatus(status);
        res.setContentType("application/json");
        new ObjectMapper().writeValue(res.getWriter(), java.util.Map.of(
                "success", false,
                "status", "EXCEPTION_ERROR",
                "message", StringUtils.defaultString(message, "Invalid JWT Token")
        ));
    }
}

