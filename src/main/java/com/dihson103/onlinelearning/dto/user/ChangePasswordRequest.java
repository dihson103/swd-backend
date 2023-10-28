package com.dihson103.onlinelearning.dto.user;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
@Builder
public class ChangePasswordRequest {

    @NotBlank(message = "Password should not be blank.")
    private String oldPassword;

    @NotBlank(message = "Password should not be blank.")
    @Length(min = 6, max = 16, message = "Password should has from 6 to 16 characters.")
    private String newPassword;

    public Boolean isPasswordValid(){
        return !oldPassword.equals(newPassword);
    }

}
