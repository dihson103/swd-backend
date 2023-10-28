package com.dihson103.onlinelearning.dto.user;

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

}
