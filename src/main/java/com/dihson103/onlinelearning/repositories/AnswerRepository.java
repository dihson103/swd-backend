package com.dihson103.onlinelearning.repositories;

import com.dihson103.onlinelearning.entities.Answer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnswerRepository extends JpaRepository<Answer, Integer> {

    List<Answer> findByQuestion_Id(Integer questionId);

}
