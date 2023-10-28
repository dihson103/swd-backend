package com.dihson103.onlinelearning.repositories;

import com.dihson103.onlinelearning.entities.Course;
import com.dihson103.onlinelearning.entities.Lesson;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class LessonRepositoryTest {

    @Autowired private LessonRepository underTest;
    @Autowired private CourseRepository courseRepository;

    @BeforeEach
    void setUp() {
        List<Course> courses = List.of(
                Course.builder()
                        .courseName("course 1")
                        .title("title 1")
                        .createdDate(LocalDateTime.now())
                        .status(true)
                        .price(11111.1)
                        .build(),
                Course.builder()
                        .courseName("course 2")
                        .title("title 2")
                        .createdDate(LocalDateTime.now())
                        .price(1111.2)
                        .status(false)
                        .build()
        );
        courseRepository.saveAll(courses);

        List<Lesson> lessons = List.of(
                Lesson.builder()
                        .title("lesson 1 - 1")
                        .content("java")
                        .status(true)
                        .course(courseRepository.findById(1).orElse(null))
                        .build(),
                Lesson.builder()
                        .title("lesson 2 - 1")
                        .content("java")
                        .status(true)
                        .course(courseRepository.findById(1).orElse(null))
                        .build(),
                Lesson.builder()
                        .title("lesson 3 - 1")
                        .content("java")
                        .status(true)
                        .course(courseRepository.findById(1).orElse(null))
                        .build(),
                Lesson.builder()
                        .title("lesson 1 - 2")
                        .content("java")
                        .status(true)
                        .course(courseRepository.findById(2).orElse(null))
                        .build(),
                Lesson.builder()
                        .title("lesson 2 - 2")
                        .content("java")
                        .status(true)
                        .course(courseRepository.findById(2).orElse(null))
                        .build(),
                Lesson.builder()
                        .title("lesson 3 - 2")
                        .content("java")
                        .status(false)
                        .course(courseRepository.findById(2).orElse(null))
                        .build()
        );
        underTest.saveAll(lessons);

    }

    @Test
    void findByIdAndActive() {
        //given
        Integer lessonId = 1;

        //when
        Lesson lesson = underTest.findByIdAndActive(lessonId).orElse(null);

        //then
        assertNotEquals(lesson, null);
    }

    @Test
    void findByIdAndActive_NotFound_CourseStatusIsFalse() {
        //given
        Integer lessonId = 4;

        //when
        Lesson lesson = underTest.findByIdAndActive(lessonId).orElse(null);

        //then
        assertEquals(lesson, null);
    }

    @Test
    void findByIdAndActive_NotFound_LessStatusIsFalse() {
        //given
        Integer lessonId = 6;

        //when
        Lesson lesson = underTest.findByIdAndActive(lessonId).orElse(null);

        //then
        assertEquals(lesson, null);
    }

    @Test
    @Disabled
    void findByCourseIdAndActive() {
    }
}