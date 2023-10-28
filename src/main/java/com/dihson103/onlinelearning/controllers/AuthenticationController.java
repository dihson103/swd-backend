package com.dihson103.onlinelearning.controllers;

import com.dihson103.onlinelearning.dto.auth.AuthenticationRequest;
import com.dihson103.onlinelearning.dto.auth.AuthenticationResponse;
import com.dihson103.onlinelearning.dto.common.ApiResponse;
import com.dihson103.onlinelearning.services.IAuthenticateService;
import jakarta.annotation.security.PermitAll;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
@PermitAll
public class AuthenticationController {

    private final IAuthenticateService service;

    @PostMapping("/login")
    public ApiResponse<AuthenticationResponse> login(@RequestBody @Valid AuthenticationRequest authenticationRequest){
        AuthenticationResponse authenticationResponse = service.authenticate(authenticationRequest);
        return ApiResponse.<AuthenticationResponse>builder()
                .message("Login success.")
                .data(authenticationResponse)
                .build();
    }

    @PostMapping("/refresh-access-token")
    public ApiResponse<AuthenticationResponse> refreshAccessToken(HttpServletRequest request,
                                                                  HttpServletResponse response) throws IOException {
        AuthenticationResponse authenticationResponse = service.refreshToken(request, response);
        return ApiResponse.<AuthenticationResponse>builder()
                .message("Refresh token success!")
                .data(authenticationResponse)
                .build();
    }

}
