package com.dihson103.onlinelearning.services;

import com.dihson103.onlinelearning.dto.filter.FilterRequestDto;
import com.dihson103.onlinelearning.dto.lesson.LessonRequest;
import com.dihson103.onlinelearning.dto.lesson.LessonResponse;

import java.util.List;

public interface ILessonService {
    void createLesson(LessonRequest lessonRequest);

    void updateLesson(Integer lessonId, LessonRequest lessonRequest);

    LessonResponse getLessonActiveById(Integer lessonId);

    LessonResponse getLesson(Integer lessonId);

    List<LessonResponse> getLessonByCourseId(Integer courseId);

    List<LessonResponse> filterLesson(FilterRequestDto filterRequestDto);
}
