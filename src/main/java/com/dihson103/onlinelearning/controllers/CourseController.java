package com.dihson103.onlinelearning.controllers;

import com.dihson103.onlinelearning.dto.common.ApiResponse;
import com.dihson103.onlinelearning.dto.course.CourseResponse;
import com.dihson103.onlinelearning.dto.course.CourseStatusRequest;
import com.dihson103.onlinelearning.dto.course.CreateCourseRequest;
import com.dihson103.onlinelearning.dto.course.UpdateCourseRequest;
import com.dihson103.onlinelearning.dto.filter.FilterRequestDto;
import com.dihson103.onlinelearning.services.ICourseService;
import jakarta.annotation.security.PermitAll;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/courses")
@RequiredArgsConstructor
public class CourseController {

    private final ICourseService service;

    @PostMapping
    @ResponseStatus(CREATED)
    @PreAuthorize("hasAnyAuthority('ADMIN', 'ADMIN_COURSE')")
    public ApiResponse createCourse(@RequestBody @Valid CreateCourseRequest courseRequest){
        service.createCourse(courseRequest);
        return ApiResponse.builder()
                .message("Course " + courseRequest.getCourseName() + " was created successfully.")
                .build();
    }

    @PutMapping
    @ResponseStatus(OK)
    @PreAuthorize("hasAnyAuthority('ADMIN', 'ADMIN_COURSE')")
    public ApiResponse updateCourse(@RequestBody @Valid UpdateCourseRequest courseRequest){
        service.updateCourse(courseRequest);
        return ApiResponse.builder()
                .message("Update course has id: " + courseRequest.getId() + " success.")
                .build();
    }

    @GetMapping("{course-id}")
    @ResponseStatus(OK)
    @PermitAll
    public ApiResponse<CourseResponse> getCourseActive(@PathVariable("course-id") Integer courseId){
        CourseResponse courseResponse = service.getCourseByIdAndStatusIsTrue(courseId);
        return ApiResponse.<CourseResponse>builder()
                .message("Get course has: " + courseId + " success.")
                .data(courseResponse)
                .build();
    }

    @GetMapping
    @ResponseStatus(OK)
    @PermitAll
    public ApiResponse<List<CourseResponse>> getCourses() {
        List<CourseResponse> courseResponses = service.getAllCourseStatusIsTrue();
        return ApiResponse.<List<CourseResponse>>builder()
                .message("Get courses success.")
                .data(courseResponses)
                .build();
    }

    @GetMapping("{course-id}/admin")
    @ResponseStatus(OK)
    @PreAuthorize("hasAnyAuthority('ADMIN', 'ADMIN_COURSE')")
    public ApiResponse<CourseResponse> getCourse(@PathVariable("course-id") Integer courseId){
        CourseResponse courseResponse = service.getCourseById(courseId);
        return ApiResponse.<CourseResponse>builder()
                .message("Get course has: " + courseId + " success.")
                .data(courseResponse)
                .build();
    }

    @PostMapping("filter")
    @ResponseStatus(OK)
    @PreAuthorize("hasAnyAuthority('ADMIN', 'ADMIN_COURSE')")
    public ApiResponse<List<CourseResponse>> filterCourses(@RequestBody FilterRequestDto filterRequestDto) {
        List<CourseResponse> courseResponses = service.filterCourses(filterRequestDto);
        return ApiResponse.<List<CourseResponse>>builder()
                .message("Filter courses success.")
                .data(courseResponses)
                .build();

    }

    @PutMapping("/change-status")
    @ResponseStatus(OK)
    @PreAuthorize("hasAnyAuthority('ADMIN', 'ADMIN_COURSE')")
    public ApiResponse changeCourseStatus(@RequestBody CourseStatusRequest courseStatusRequest){
        service.changeCourseStatus(courseStatusRequest);
        return ApiResponse.builder()
                .message("Change course status to " + (courseStatusRequest.getStatus() ? "active" : "inactive") + " success")
                .build();
    }

    @GetMapping("newest")
    @ResponseStatus(OK)
    @PermitAll
    public ApiResponse<List<CourseResponse>> getTopNewCourses(){
        List<CourseResponse> courseResponses = service.findTopNewCourses();
        return ApiResponse.<List<CourseResponse>>builder()
                .message("Get top 5 newest courses success.")
                .data(courseResponses)
                .build();
    }

    @GetMapping()
    @ResponseStatus(OK)
    @PermitAll
    public ApiResponse<List<CourseResponse>> searchCourses(@RequestParam("search-value") String searchValue){
        List<CourseResponse> courseResponses = service.searchCourses(searchValue);
        return ApiResponse.<List<CourseResponse>>builder()
                .message("Get top 5 newest courses success.")
                .data(courseResponses)
                .build();
    }

}
