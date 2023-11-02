package com.dihson103.onlinelearning.dto.user;

import com.dihson103.onlinelearning.entities.Role;
import lombok.*;

import java.util.Date;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {

    private Integer id;
    private String username;
    private String email;
    private Date dob;
    private String phone;
    private String address;
    private Role role;

}
