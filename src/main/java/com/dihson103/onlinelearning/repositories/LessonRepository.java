package com.dihson103.onlinelearning.repositories;

import com.dihson103.onlinelearning.entities.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import javax.crypto.spec.OAEPParameterSpec;
import java.util.List;
import java.util.Optional;

public interface LessonRepository extends JpaRepository<Lesson, Integer>, JpaSpecificationExecutor<Lesson> {

    @Query("""
            SELECT l FROM Lesson l 
            WHERE l.id = :lessonId AND l.status = true AND l.course.status = true
        """)
    Optional<Lesson> findByIdAndActive(Integer lessonId);

    @Query("""
            SELECT l FROM Lesson l 
            WHERE l.course.id = :courseId AND l.course.status = true AND l.status = true
        """)
    List<Lesson> findByCourseIdAndActive(Integer courseId);



}
