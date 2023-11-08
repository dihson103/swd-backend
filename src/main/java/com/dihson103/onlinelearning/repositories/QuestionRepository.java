package com.dihson103.onlinelearning.repositories;

import com.dihson103.onlinelearning.entities.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Integer>,
        JpaSpecificationExecutor<Question> {

    List<Question> findBySession_Id(Integer sessionId);

}
