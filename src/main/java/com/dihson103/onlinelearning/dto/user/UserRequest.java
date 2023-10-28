package com.dihson103.onlinelearning.dto.user;

import com.dihson103.onlinelearning.entities.Role;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRequest {

        @NotBlank(message = "User name must not be blank.")
        private String username;

        @Email(message = "Wrong email format.")
        private String email;

        @Length(min = 6, max = 160, message = "Password must have at least 6 characters and less than 160 characters.")
        private String password;

        private Date dob;

        @Pattern(regexp = "^\\d{10}$", message = "Wrong phone number format.")
        private String phone;

        private String address;
}
