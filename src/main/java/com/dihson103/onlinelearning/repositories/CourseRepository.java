package com.dihson103.onlinelearning.repositories;

import com.dihson103.onlinelearning.entities.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface CourseRepository extends JpaRepository<Course, Integer>, JpaSpecificationExecutor<Course> {

    Optional<Course> findByIdAndStatusIsTrue(Integer courseId);

    List<Course> findByStatusIsTrue();

}
