package vn.trivd.hospitalgateway.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterCompleteReq {
    @NotBlank
    private String registrationToken;
    @NotBlank
    private String userType;
    @Email @NotBlank
    private String email;
    @NotBlank
    private String fullName;
    @NotBlank
    private String password;
}

