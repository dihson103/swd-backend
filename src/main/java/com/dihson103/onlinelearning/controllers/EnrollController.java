package com.dihson103.onlinelearning.controllers;

import com.dihson103.onlinelearning.dto.common.ApiResponse;
import com.dihson103.onlinelearning.dto.enroll.EnrollResponse;
import com.dihson103.onlinelearning.services.IEnrollService;
import jakarta.annotation.security.PermitAll;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("api/enrolls")
@RequiredArgsConstructor
public class EnrollController {

    private final IEnrollService service;

    @PostMapping("{course-id}")
    @ResponseStatus(CREATED)
    @PreAuthorize("hasAuthority('USER')")
    public ApiResponse enrollCourse(@PathVariable("course-id") Integer courseId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        service.enrollCourse(username, courseId);
        return ApiResponse.builder()
                .message("Enroll course has id: " + courseId + " success.")
                .build();
    }

    @GetMapping("{username}")
    @ResponseStatus(OK)
    @PreAuthorize("hasAuthority('ADMIN') || #username == authentication.getName()")
    public ApiResponse<List<EnrollResponse>> getListEnrollByUser(@PathVariable String username){
        List<EnrollResponse> enrollResponses = service.getEnrollListByUser(username);
        return ApiResponse.<List<EnrollResponse>>builder()
                .message("Get list enroll of user has username: " + username + " success.")
                .data(enrollResponses)
                .build();
    }

    @GetMapping("/search")
    @ResponseStatus(OK)
    @PermitAll
    public ApiResponse<List<EnrollResponse>> searchEnrolledCourses(@RequestParam String searchValue){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        List<EnrollResponse> enrollResponses = service.searchEnrolledCourses(username, searchValue);
        return ApiResponse.<List<EnrollResponse>>builder()
                .message("Get list enroll of user has username: " + username + " success.")
                .data(enrollResponses)
                .build();
    }

}
