package com.dihson103.onlinelearning.controllers;

import com.dihson103.onlinelearning.dto.common.ApiResponse;
import com.dihson103.onlinelearning.dto.filter.FilterRequestDto;
import com.dihson103.onlinelearning.dto.session.SessionRequest;
import com.dihson103.onlinelearning.dto.session.SessionResponse;
import com.dihson103.onlinelearning.dto.user.UserResponse;
import com.dihson103.onlinelearning.services.ISessionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/sessions")
public class SessionController {

    private final ISessionService service;

    @PostMapping
    @ResponseStatus(CREATED)
    @PreAuthorize("hasAnyAuthority('ADMIN', 'ADMIN_COURSE')")
    public ApiResponse createSession(@Valid @RequestBody SessionRequest sessionRequest){
        service.createSession(sessionRequest);
        return ApiResponse.builder()
                .message("Create session success.")
                .build();
    }

    @PutMapping("{session-id}")
    @ResponseStatus(OK)
    @PreAuthorize("hasAnyAuthority('ADMIN', 'ADMIN_COURSE')")
    public ApiResponse updateSession(@Valid @RequestBody SessionRequest sessionRequest,
                                     @PathVariable("session-id") Integer sessionId){
        service.updateSession(sessionId, sessionRequest);
        return ApiResponse.builder()
                .message("Create session success.")
                .build();
    }

    @GetMapping({"session-id"})
    @ResponseStatus(OK)
    public ApiResponse<SessionResponse> getSession(@PathVariable("session-id") Integer sessionId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        SessionResponse sessionResponse = service.getSessionActiveById(sessionId, username);
        return ApiResponse.<SessionResponse>builder()
                .message("Get session has id: " + sessionId + " success.")
                .data(sessionResponse)
                .build();
    }

    @GetMapping("lesson-id/lesson")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'ADMIN_COURSE')")
    public ApiResponse<List<SessionResponse>> getSessionsByLesson(@PathVariable("lesson-id") Integer lessonId){
        List<SessionResponse> sessionResponses = service.getSessionsActiveByLesson(lessonId);
        return ApiResponse.<List<SessionResponse>>builder()
                .message("Get sessions of lesson has id: " + lessonId + " success.")
                .data(sessionResponses)
                .build();
    }

    @PostMapping("specification")
    public ApiResponse<List<SessionResponse>> filterSession(@RequestBody FilterRequestDto filterRequestDto){
        List<SessionResponse> sessionResponses = service.filter(filterRequestDto);
        return ApiResponse.<List<SessionResponse>>builder()
                .message("Filter session success.")
                .data(sessionResponses)
                .build();
    }

}
