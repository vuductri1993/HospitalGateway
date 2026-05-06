package vn.trivd.hospitalgateway.auth.dto;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRes {
    private String accessToken;
    private UserProfile user;
    private List<String> permissions;

    public LoginRes() {}

    public LoginRes(String accessToken, UserProfile user, List<String> permissions) {
        this.accessToken = accessToken;
        this.user = user;
        this.permissions = permissions;
    }

    @Getter
    @Setter
    public static class UserProfile {
        private Long userId;
        private String fullName;
        private String email;
        private String title;
        private Long departmentId;
        private boolean departmentHead;

        public UserProfile() {}

        public UserProfile(Long userId, String fullName, String email, String title, Long departmentId, boolean departmentHead) {
            this.userId = userId;
            this.fullName = fullName;
            this.email = email;
            this.title = title;
            this.departmentId = departmentId;
            this.departmentHead = departmentHead;
        }
    }
}

