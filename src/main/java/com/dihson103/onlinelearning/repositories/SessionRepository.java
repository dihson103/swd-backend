package com.dihson103.onlinelearning.repositories;

import com.dihson103.onlinelearning.entities.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface SessionRepository extends JpaRepository<Session, Integer>, JpaSpecificationExecutor<Session> {

    Optional<Session> findByLesson_IdAndSessionName(Integer lessonId, String sessionName);

    Optional<Session> findByIdAndStatusIsTrue(Integer sessionId);

    List<Session> findByLesson_IdAndStatusIsTrue(Integer lessonId);

}
