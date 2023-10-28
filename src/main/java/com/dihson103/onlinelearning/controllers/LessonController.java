package com.dihson103.onlinelearning.controllers;

import com.dihson103.onlinelearning.dto.common.ApiResponse;
import com.dihson103.onlinelearning.dto.filter.FilterRequestDto;
import com.dihson103.onlinelearning.dto.lesson.LessonRequest;
import com.dihson103.onlinelearning.dto.lesson.LessonResponse;
import com.dihson103.onlinelearning.services.ILessonService;
import jakarta.annotation.security.PermitAll;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("api/lessons")
@RequiredArgsConstructor
public class LessonController {

    private final ILessonService service;

    @PostMapping
    @ResponseStatus(CREATED)
    @PreAuthorize("hasAnyAuthority('ADMIN', 'ADMIN_COURSE')")
    public ApiResponse createLesson(@Valid @RequestBody LessonRequest lessonRequest){
        service.createLesson(lessonRequest);
        return ApiResponse.builder()
                .message("Create lesson success.")
                .build();
    }

    @PutMapping("{lesson-id}")
    @ResponseStatus(OK)
    @PreAuthorize("hasAnyAuthority('ADMIN', 'ADMIN_COURSE')")
    public ApiResponse updateLesson(@Valid @RequestBody LessonRequest lessonRequest,
                                    @PathVariable("lesson-id") Integer lessonId){
        service.updateLesson(lessonId, lessonRequest);
        return ApiResponse.builder()
                .message("Create lesson success.")
                .build();
    }

    @GetMapping("{lesson-id}")
    @ResponseStatus(OK)
    @PermitAll
    public ApiResponse<LessonResponse> getLessonActive(@PathVariable("lesson-id") Integer lessonId){
        LessonResponse lessonResponse = service.getLessonActiveById(lessonId);
        return ApiResponse.<LessonResponse>builder()
                .message("Get lesson has id: " + lessonId + " success.")
                .data(lessonResponse)
                .build();
    }

    @GetMapping("{course-id}/course")
    @ResponseStatus(OK)
    @PermitAll
    public ApiResponse<List<LessonResponse>> getLessonByCourseId(@PathVariable("course-id") Integer courseId){
        List<LessonResponse> lessonResponses = service.getLessonByCourseId(courseId);
        return ApiResponse.<List<LessonResponse>>builder()
                .message("Get list lesson active success.")
                .data(lessonResponses)
                .build();
    }

    @GetMapping("{lesson-id}/admin")
    @ResponseStatus(OK)
    @PreAuthorize("hasAnyAuthority('ADMIN', 'ADMIN_COURSE')")
    public ApiResponse<LessonResponse> getLesson(@PathVariable("lesson-id") Integer lessonId){
        LessonResponse lessonResponse = service.getLesson(lessonId);
        return ApiResponse.<LessonResponse>builder()
                .message("Get lesson has id: " + lessonId + " success.")
                .data(lessonResponse)
                .build();
    }

    @PostMapping("filter")
    @ResponseStatus(OK)
    @PreAuthorize("hasAnyAuthority('ADMIN', 'ADMIN_COURSE')")
    public ApiResponse<List<LessonResponse>> filterLesson(@RequestBody FilterRequestDto filterRequestDto){
        List<LessonResponse> lessonResponses = service.filterLesson(filterRequestDto);
        return ApiResponse.<List<LessonResponse>>builder()
                .message("Filter lesson success.")
                .data(lessonResponses)
                .build();
    }



}
