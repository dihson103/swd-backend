package com.dihson103.onlinelearning.services.impl;

import com.dihson103.onlinelearning.dto.filter.FilterRequestDto;
import com.dihson103.onlinelearning.dto.lesson.LessonRequest;
import com.dihson103.onlinelearning.dto.lesson.LessonResponse;
import com.dihson103.onlinelearning.entities.Course;
import com.dihson103.onlinelearning.entities.Lesson;
import com.dihson103.onlinelearning.repositories.CourseRepository;
import com.dihson103.onlinelearning.repositories.LessonRepository;
import com.dihson103.onlinelearning.services.FiltersSpecification;
import com.dihson103.onlinelearning.services.ILessonService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LessonService implements ILessonService {

    private final LessonRepository lessonRepository;
    private final CourseRepository courseRepository;
    private final ModelMapper modelMapper;
    private final FiltersSpecification<Lesson> filtersSpecification;

    private Course findLessonCourse(Integer courseId){
        return courseRepository.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("Can not find course has id: " + courseId));
    }

    private Lesson getLessonByLessonRequest(LessonRequest lessonRequest){
        Course course = findLessonCourse(lessonRequest.getCourseId());
        if(course.getStatus() == false && lessonRequest.getStatus()){
            throw new IllegalArgumentException("This lesson should not be active because the course is inactive");
        }
        Lesson lesson = modelMapper.map(lessonRequest, Lesson.class);
        lesson.setCourse(course);
        return lesson;
    }

    @Override
    public void createLesson(LessonRequest lessonRequest) {
        Lesson lesson = getLessonByLessonRequest(lessonRequest);
        lessonRepository.save(lesson);
    }

    @Override
    public void updateLesson(Integer lessonId, LessonRequest lessonRequest) {
        Lesson lesson = getLessonById(lessonId);
        Course course = findLessonCourse(lessonRequest.getCourseId());
        if(course.getStatus() == false && lessonRequest.getStatus()){
            throw new IllegalArgumentException("This lesson should not be active because the course is inactive");
        }
        lesson.setCourse(course);
        lesson.setStatus(lessonRequest.getStatus());
        lesson.setTitle(lessonRequest.getTitle());
        lesson.setContent(lessonRequest.getContent());
        lessonRepository.save(lesson);
    }

    private Lesson getLessonById(Integer lessonId){
        return lessonRepository.findById(lessonId)
                .orElseThrow(() -> new IllegalArgumentException("Can not find lesson has id: " + lessonId));
    }

    @Override
    public LessonResponse getLessonActiveById(Integer lessonId) {
        Lesson lesson = lessonRepository.findByIdAndActive(lessonId)
                .orElseThrow(() -> new IllegalArgumentException("Can not find lesson has id: " + lessonId ));
        return modelMapper.map(lesson, LessonResponse.class);
    }

    @Override
    public LessonResponse getLesson(Integer lessonId) {
        Lesson lesson = getLessonById(lessonId);
        return modelMapper.map(lesson, LessonResponse.class);
    }

    @Override
    public List<LessonResponse> getLessonByCourseId(Integer courseId) {
        List<Lesson> lessons = lessonRepository.findByCourseIdAndActive(courseId);
        return lessons.stream()
                .map(lesson -> modelMapper.map(lesson, LessonResponse.class))
                .toList();
    }

    @Override
    public List<LessonResponse> filterLesson(FilterRequestDto filterRequestDto) {
        Specification specification = filtersSpecification.getSpecification(
                filterRequestDto.getSearchRequestDto(),
                filterRequestDto.getGlobalOperator()
        );
        List<Lesson> lessons = lessonRepository.findAll(specification);
        return lessons.stream()
                .map(lesson -> modelMapper.map(lesson, LessonResponse.class))
                .toList();
    }
}
