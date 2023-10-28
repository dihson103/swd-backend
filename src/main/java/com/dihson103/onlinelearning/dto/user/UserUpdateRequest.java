package com.dihson103.onlinelearning.dto.user;


import com.dihson103.onlinelearning.entities.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserUpdateRequest {

    @NotNull(message = "Userid can not be empty.")
    private Integer id;

    @NotBlank(message = "User name must not be blank.")
    private String username;

    @Email(message = "Wrong email format.")
    private String email;

    private Date dob;

    @Pattern(regexp = "^\\d{10}$", message = "Wrong phone number format.")
    private String phone;

    private String address;

}
