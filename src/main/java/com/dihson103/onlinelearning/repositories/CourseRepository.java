package com.dihson103.onlinelearning.repositories;

import com.dihson103.onlinelearning.entities.Course;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CourseRepository extends JpaRepository<Course, Integer>, JpaSpecificationExecutor<Course> {

    Optional<Course> findByIdAndStatusIsTrue(Integer courseId);

    List<Course> findByStatusIsTrue();

    @Query("""
    SELECT c FROM Course c WHERE c.status = true ORDER BY c.id DESC
""")
    List<Course> findTopNewest(Pageable pageable);

    @Query("""
    SELECT c FROM Course c WHERE c.status = true AND c.courseName LIKE %:searchValue%
""")
    List<Course> findByStatusAndCourseName(@Param("searchValue") String searchValue);

}
