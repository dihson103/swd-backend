package com.dihson103.onlinelearning.services;

import com.dihson103.onlinelearning.dto.filter.FilterRequestDto;
import com.dihson103.onlinelearning.dto.user.ChangePasswordRequest;
import com.dihson103.onlinelearning.dto.user.UserRequest;
import com.dihson103.onlinelearning.dto.user.UserResponse;
import com.dihson103.onlinelearning.dto.user.UserUpdateRequest;

import java.util.List;

public interface IUserService {

    void register(UserRequest userRequest);

    List<UserResponse> getUsers();

    void createUser(UserRequest userRequest, String role);

    void deleteUser(Integer userId);

    UserResponse getUserByUsername(String username);

    void updateUser(String username, UserUpdateRequest userRequest);

    void updateUserWithRole(UserUpdateRequest userUpdateRequest, String role);

    List<UserResponse> filter(FilterRequestDto requestDto);

    void changePassword(String username, ChangePasswordRequest changePasswordRequest);

    List<UserResponse> getListUserEnrollCourse(Integer courseId);
}
