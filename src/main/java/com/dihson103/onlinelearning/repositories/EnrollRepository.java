package com.dihson103.onlinelearning.repositories;

import com.dihson103.onlinelearning.entities.Enroll;
import com.dihson103.onlinelearning.entities.EnrollKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface EnrollRepository extends JpaRepository<Enroll, EnrollKey>, JpaSpecificationExecutor<Enroll> {

    @Query("""
        SELECT e FROM Enroll e 
        WHERE e.user.username = :username
    """)
    List<Enroll> getListEnrollByUsername(String username);

    @Query("""
        SELECT e FROM Enroll e
        WHERE e.course.id = :courseId   
    """)
    List<Enroll> getListEnrollByCourseId(Integer courseId);


    @Query("""
        SELECT e FROM Enroll e
        WHERE e.course.id = :courseId AND e.user.username = :username
    """)
    Optional<Enroll> getEnrollByUsernameAndCourse(String username, Integer courseId);

    @Query("""
        SELECT e FROM Enroll e 
        WHERE e.user.username = :username AND e.course.courseName  LIKE %:searchValue%
    """)
    List<Enroll> getEnrollsByUsernameAndSearch(String username, String searchValue);

}
