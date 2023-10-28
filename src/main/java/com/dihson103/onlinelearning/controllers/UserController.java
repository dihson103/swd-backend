package com.dihson103.onlinelearning.controllers;

import com.dihson103.onlinelearning.dto.common.ApiResponse;
import com.dihson103.onlinelearning.dto.filter.FilterRequestDto;
import com.dihson103.onlinelearning.dto.user.ChangePasswordRequest;
import com.dihson103.onlinelearning.dto.user.UserRequest;
import com.dihson103.onlinelearning.dto.user.UserResponse;
import com.dihson103.onlinelearning.dto.user.UserUpdateRequest;
import com.dihson103.onlinelearning.entities.Role;
import com.dihson103.onlinelearning.entities.UserEntity;
import com.dihson103.onlinelearning.repositories.UserRepository;
import com.dihson103.onlinelearning.services.IUserService;
import com.dihson103.onlinelearning.services.impl.UserService;
import jakarta.annotation.security.PermitAll;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final IUserService service;

    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping
    @ResponseStatus(CREATED)
    @PermitAll
    public ApiResponse register(@Valid @RequestBody UserRequest userRequest){
        service.register(userRequest);
        return ApiResponse.builder()
                .message("Create account success.")
                .build();
    }

    @GetMapping
    @ResponseStatus(OK)
    @PreAuthorize("hasAnyAuthority('ADMIN', 'ADMIN_USER')")
    public ApiResponse<List<UserResponse>> getAllUsers(){
        List<UserResponse> users = service.getUsers();
        return ApiResponse.<List<UserResponse>>builder()
                .message("Get data success.")
                .data(users)
                .build();
    }

    @PostMapping("{user-role}")
    @ResponseStatus(CREATED)
    @PreAuthorize("hasAnyAuthority('ADMIN', 'ADMIN_USER')")
    public ApiResponse createUser(@Valid @RequestBody UserRequest userRequest,
                                  @PathVariable("user-role") String role){
        service.createUser(userRequest, role);
        return ApiResponse.builder()
                .message("Create account success.")
                .build();
    }

    @DeleteMapping("{user-id}")
    @ResponseStatus(OK)
    @PreAuthorize("hasAnyAuthority('ADMIN', 'ADMIN_USER')")
    public ApiResponse deleteUser(@PathVariable("user-id") Integer userId){
        service.deleteUser(userId);
        return ApiResponse.builder()
                .message("Delete user has id: " + userId + " success.")
                .build();
    }

    @GetMapping("{username}")
    @ResponseStatus(OK)
    @PreAuthorize("hasAnyAuthority('ADMIN', 'ADMIN_USER') || #username == authentication.getName()")
    public ApiResponse<UserResponse> getUser(@PathVariable String username){
        UserResponse user = service.getUserByUsername(username);
        return ApiResponse.<UserResponse>builder()
                .message("Get user success.")
                .data(user)
                .build();
    }

    @PutMapping ("{username}")
    @ResponseStatus(OK)
    @PreAuthorize("#username == authentication.getName()")
    public ApiResponse updateUser(@PathVariable String username,
                                  @Valid @RequestBody UserUpdateRequest userRequest){
        service.updateUser(username, userRequest);
        return ApiResponse.builder()
                .message("Update user success.")
                .build();
    }

    @PutMapping("/update-with-role/{user-role}")
    @ResponseStatus(OK)
    @PreAuthorize("hasAnyAuthority('ADMIN', 'ADMIN_USER')")
    public ApiResponse updateUserWithRole(@Valid @RequestBody UserUpdateRequest userRequest,
                                          @PathVariable("user-role") String role){
        service.updateUserWithRole(userRequest, role);
        return ApiResponse.builder()
                .message("Update user success.")
                .build();
    }

    @PostMapping("/specification")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'ADMIN_USER')")
    public ApiResponse<List<UserResponse>> filterUser(@RequestBody FilterRequestDto requestDto){
        List<UserResponse> userResponses = service.filter(requestDto);
        return ApiResponse.<List<UserResponse>>builder()
                .message("Filter users success.")
                .data(userResponses)
                .build();
    }

    @PutMapping("/change-password/{username}")
    @ResponseStatus(OK)
    @PreAuthorize("#username == authentication.getName()")
    public ApiResponse changePassword(@PathVariable String username,
                               @Valid @RequestBody ChangePasswordRequest changePasswordRequest){
        service.changePassword(username, changePasswordRequest);
        return ApiResponse.builder()
                .message("Change password success.")
                .build();
    }

    @GetMapping("{course-id}/course")
    @ResponseStatus(OK)
    @PreAuthorize("hasAnyAuthority('ADMIN', 'ADMIN_COURSE')")
    public ApiResponse<List<UserResponse>> getUsersEnrollCourse(@PathVariable("course-id") Integer courseId){
        List<UserResponse> userResponses = service.getListUserEnrollCourse(courseId);
        return ApiResponse.<List<UserResponse>>builder()
                .message("Get list user enroll course has id: " + courseId + " success.")
                .data(userResponses)
                .build();
    }

}
