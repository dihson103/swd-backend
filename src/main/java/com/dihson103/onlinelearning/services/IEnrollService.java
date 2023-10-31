package com.dihson103.onlinelearning.services;

import com.dihson103.onlinelearning.dto.enroll.EnrollResponse;

import java.util.List;

public interface IEnrollService {
    void enrollCourse(String username, Integer courseId);

    List<EnrollResponse> getEnrollListByUser(String username);

    List<EnrollResponse> searchEnrolledCourses(String username, String searchValue);
}
