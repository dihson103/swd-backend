package com.dihson103.onlinelearning.dto.auth;

import com.dihson103.onlinelearning.dto.user.UserResponse;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse{

        @JsonProperty("access_token")
        private String accessToken;

        @JsonProperty("refresh_token")
        private String refreshToken;

        @JsonProperty("user_response")
        private UserResponse userResponse;

}
