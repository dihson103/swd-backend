package com.dihson103.onlinelearning.controllers;

import com.amazonaws.HttpMethod;
import com.dihson103.onlinelearning.dto.common.ApiResponse;
import com.dihson103.onlinelearning.services.FileService;
import com.dihson103.onlinelearning.services.IEnrollService;
import com.dihson103.onlinelearning.services.impl.EnrollService;
import jakarta.annotation.security.PermitAll;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/videos")
public class AwsS3Controller {

    private final FileService fileService;
    private final IEnrollService enrollService;

    @PostMapping("/get-url")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'ADMIN_COURSE')")
    public ResponseEntity<String> generateUrl(@RequestParam String extension) {
        return ResponseEntity.ok(fileService.generatePreSignedUrl(
                UUID.randomUUID()+"."+extension, HttpMethod.PUT));
    }

    @GetMapping("/get-url")
    @PermitAll
    public ApiResponse getUrl(@RequestParam String filename,
                              @RequestParam(name = "course-id", required = false) Integer courseId) {
        if(courseId != null){
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            enrollService.checkVideoPermission(username, courseId);
        }
        return ApiResponse.builder()
                .message(fileService.generatePreSignedUrl(filename, HttpMethod.GET))
                .build();
    }

}
