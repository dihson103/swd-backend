package com.dihson103.onlinelearning.services;

import com.dihson103.onlinelearning.dto.question.QuestionRequest;
import com.dihson103.onlinelearning.dto.question.QuestionResponse;

import java.util.List;

public interface IQuestionService {
    void createQuestion(QuestionRequest questionRequest);

    void updateQuestion(Integer questionId, QuestionRequest questionRequest);

    QuestionResponse findQuestionResponseById(Integer questionId);

    List<QuestionResponse> findQuestionsBySessionId(Integer sessionId);
}
