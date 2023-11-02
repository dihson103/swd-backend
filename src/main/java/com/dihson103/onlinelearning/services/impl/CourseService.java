package com.dihson103.onlinelearning.services.impl;

import com.amazonaws.HttpMethod;
import com.dihson103.onlinelearning.dto.course.CourseResponse;
import com.dihson103.onlinelearning.dto.course.CourseStatusRequest;
import com.dihson103.onlinelearning.dto.course.CreateCourseRequest;
import com.dihson103.onlinelearning.dto.course.UpdateCourseRequest;
import com.dihson103.onlinelearning.dto.filter.FilterRequestDto;
import com.dihson103.onlinelearning.entities.Course;
import com.dihson103.onlinelearning.entities.Lesson;
import com.dihson103.onlinelearning.entities.Session;
import com.dihson103.onlinelearning.repositories.CourseRepository;
import com.dihson103.onlinelearning.repositories.LessonRepository;
import com.dihson103.onlinelearning.repositories.SessionRepository;
import com.dihson103.onlinelearning.services.FileService;
import com.dihson103.onlinelearning.services.FiltersSpecification;
import com.dihson103.onlinelearning.services.ICourseService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CourseService implements ICourseService {

    private final CourseRepository courseRepository;
    private final LessonRepository lessonRepository;
    private final SessionRepository sessionRepository;
    private final FileService fileService;
    private final ModelMapper modelMapper;
    private final FiltersSpecification<Course> filtersSpecification;

    @Override
    public void createCourse(CreateCourseRequest courseRequest) {
        Course course = modelMapper.map(courseRequest, Course.class);
        course.setCreatedDate(LocalDateTime.now());
        course.setStatus(false);
        courseRepository.save(course);
    }

    private Course findCourseById(Integer courseId){
        return courseRepository.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("Can not find course has id: " + courseId));
    }

    @Override
    public void updateCourse(UpdateCourseRequest courseRequest) {
        Course course = findCourseById(courseRequest.getId());
        course.setCourseName(courseRequest.getCourseName());
        course.setPrice(courseRequest.getPrice());
        course.setTitle(courseRequest.getTitle());
        course.setImage(course.getImage());
        course.setStatus(false);
        courseRepository.save(course);
    }

    @Override
    public CourseResponse getCourseById(Integer courseId) {
        Course course = findCourseById(courseId);
        return modelMapper.map(course, CourseResponse.class);
    }

    @Override
    public CourseResponse getCourseByIdAndStatusIsTrue(Integer courseId) {
        Course course = courseRepository.findByIdAndStatusIsTrue(courseId)
                .orElseThrow(() -> new IllegalArgumentException("Can not find course has id: " + courseId));
        return modelMapper.map(course, CourseResponse.class);
    }

    @Override
    public List<CourseResponse> getAllCourseStatusIsTrue() {
        List<Course> courses = courseRepository.findByStatusIsTrue();
        //TODO get image's link
        courses.stream().forEach(course -> {
            course.setImage(fileService.generatePreSignedUrl(course.getImage(), HttpMethod.GET));
        });
        return courses.stream()
                .map(course -> modelMapper.map(course, CourseResponse.class))
                .toList();
    }

    @Override
    public List<CourseResponse> filterCourses(FilterRequestDto filterRequestDto) {
        Specification specification = filtersSpecification.getSpecification(
                filterRequestDto.getSearchRequestDto(),
                filterRequestDto.getGlobalOperator()
        );
        List<Course> courses = courseRepository.findAll(specification);
        return courses.stream()
                .map(course -> modelMapper.map(course, CourseResponse.class))
                .toList();
    }

    @Override
    public void changeCourseStatus(CourseStatusRequest courseStatusRequest) {
        Course course = courseRepository.findById(courseStatusRequest.getCourseId())
                .orElseThrow(() -> new IllegalArgumentException("Can not find course has id " + courseStatusRequest.getCourseId()));
        if(!courseStatusRequest.getStatus()){
            updateAllStatusToFalse(course);
            return;
        }
        updateAllRequestToTrue(courseStatusRequest, course);
    }

    @Override
    public List<CourseResponse> findTopNewCourses() {
        Pageable top5 = PageRequest.of(0, 5, Sort.by("id").descending());
        List<Course> top5NewestCourses = courseRepository.findTopNewest(top5);
        return top5NewestCourses
                .stream()
                .map(course -> modelMapper.map(course, CourseResponse.class))
                .toList();
    }

    @Override
    public List<CourseResponse> searchCourses(String searchValue) {
        List<Course> courses = courseRepository.findByStatusAndCourseName(searchValue);
        courses.stream().forEach(course -> {
            course.setImage(fileService.generatePreSignedUrl(course.getImage(), HttpMethod.GET));
        });
        return courses
                .stream()
                .map(course -> modelMapper.map(course, CourseResponse.class))
                .toList();
    }

    private void updateAllRequestToTrue(CourseStatusRequest courseStatusRequest, Course course ) {
        List<Lesson> lessons = getLessons(courseStatusRequest.getLessonIds());
        List<Session> sessions = getSessions(courseStatusRequest.getSessionIds());
        updateStatusAll(course, lessons, sessions, true);
    }

    private void updateAllStatusToFalse(Course course) {
        List<Lesson> lessons = course.getLessons();
        List<Session> sessions = lessons.stream()
                .flatMap(lesson -> lesson.getSessions().stream())
                .toList();
        updateStatusAll(course, lessons, sessions, false);
    }

    private void updateStatusAll(Course course, List<Lesson> lessons, List<Session> sessions, Boolean status) {
        course.setStatus(status);
        if(status){
            course.setPublicDate(LocalDateTime.now());
        }
        List<Lesson> lessonsUpdate = lessons
                .stream()
                .map(lesson -> {
                    lesson.setStatus(status);
                    return lesson;
                })
                .toList();
        List<Session> sessionsUpdate = sessions
                .stream()
                .map(session -> {
                    if(session.getLesson().getStatus() == false && status){
                        throw new IllegalArgumentException("Please active lesson to active session.");
                    }
                    session.setStatus(status);
                    return session;
                }).toList();
        courseRepository.save(course);
        lessonRepository.saveAll(lessonsUpdate);
        sessionRepository.saveAll(sessionsUpdate);
    }

    private List<Lesson> getLessons(List<Integer> lessonIds){
        return lessonIds.stream()
                .map(lessonId -> lessonRepository.findById(lessonId)
                        .orElseThrow(() -> new IllegalArgumentException("Can not find lesson has id " + lessonId)))
                .toList();
    }

    private List<Session> getSessions(List<Integer> sessionIds){
        return sessionIds.stream()
                .map(sessionId -> sessionRepository.findById(sessionId)
                        .orElseThrow(() -> new IllegalArgumentException("Can not find session has id " + sessionId)))
                .toList();
    }
}
