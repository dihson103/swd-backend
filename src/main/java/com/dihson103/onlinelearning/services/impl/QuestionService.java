package com.dihson103.onlinelearning.services.impl;

import com.dihson103.onlinelearning.dto.answer.AnswerRequest;
import com.dihson103.onlinelearning.dto.answer.AnswerResponse;
import com.dihson103.onlinelearning.dto.question.QuestionRequest;
import com.dihson103.onlinelearning.dto.question.QuestionResponse;
import com.dihson103.onlinelearning.entities.Answer;
import com.dihson103.onlinelearning.entities.Question;
import com.dihson103.onlinelearning.entities.Session;
import com.dihson103.onlinelearning.repositories.AnswerRepository;
import com.dihson103.onlinelearning.repositories.QuestionRepository;
import com.dihson103.onlinelearning.repositories.SessionRepository;
import com.dihson103.onlinelearning.services.FiltersSpecification;
import com.dihson103.onlinelearning.services.IQuestionService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class QuestionService implements IQuestionService {

    private final QuestionRepository questionRepository;
    private final SessionRepository sessionRepository;
    private final AnswerRepository answerRepository;
    private final ModelMapper modelMapper;

    private Session findSessionById(Integer sessionId){
        return sessionRepository.findById(sessionId)
                .orElseThrow(() ->  new IllegalArgumentException("Can not find session has id: " + sessionId));
    }

    private Question addNewQuestion(QuestionRequest questionRequest){
        Session session = findSessionById(questionRequest.getSessionId());
        Question question = modelMapper.map(questionRequest, Question.class);
        question.setSession(session);
        question.setStatus(false);
        return questionRepository.save(question);
    }

    private void addNewAnswers(Question question, List<AnswerRequest> answerRequests){
        List<Answer> answers = answerRequests
                .stream()
                .map(answerRequest -> {
                    Answer answer = modelMapper.map(answerRequest, Answer.class);
                    answer.setQuestion(question);
                    answer.setStatus(true);
                    return answer;
                })
                .toList();
        answerRepository.saveAll(answers);
    }

    @Override
    @Transactional(rollbackFor = {IllegalArgumentException.class})
    public void createQuestion(QuestionRequest questionRequest) {
        if(questionRequest.getAnswers().size() < 2){
            throw new IllegalArgumentException("Each question should have at least 2 answers.");
        }
        Question newQuestion = addNewQuestion(questionRequest);
        addNewAnswers(newQuestion, questionRequest.getAnswers());
    }

    private Question findQuestionById(Integer questionId){
        return questionRepository.findById(questionId)
                .orElseThrow(() -> new IllegalArgumentException("Can not find question has id: " + questionId));
    }

    private List<Answer> findAnswersByQuestionId(Integer questionId){
        List<Answer> answers = answerRepository.findByQuestion_Id(questionId);
        if(answers.isEmpty()){
            throw new IllegalArgumentException("Can not find answers by question has id: " + questionId);
        }
        return answers;
    }

    private void updateQuestionToDb(Integer questionId, QuestionRequest questionRequest){
        Question question = findQuestionById(questionId);
        question.setQuestion(questionRequest.getQuestion());
        question.setExplain(questionRequest.getExplain());
        questionRepository.save(question);
    }

    private void updateAnswers(Integer questionId, List<AnswerRequest> answerRequests){
        List<Answer> answers = findAnswersByQuestionId(questionId);

        if(answerRequests.size() > answers.size()){
            //TODO update and add answers to db
        }
        else if(answerRequests.size() < answers.size()){
            //TODO update and remove answers to db
        }
        else {
            //TODO update only
        }
    }

    @Override
    @Transactional(rollbackFor = {IllegalArgumentException.class, Error.class})
    public void updateQuestion(Integer questionId, QuestionRequest questionRequest) {
        if(questionRequest.getAnswers().size() < 2){
            throw new IllegalArgumentException("Each question should have at least 2 answers.");
        }
        updateQuestionToDb(questionId, questionRequest);
        updateAnswers(questionId, questionRequest.getAnswers());
    }

    @Override
    public QuestionResponse findQuestionResponseById(Integer questionId) {
        Question question = findQuestionById(questionId);
        return convert(question);
    }

    private QuestionResponse convert(Question question){
        List<AnswerResponse> answerResponses = question.getAnswers()
                .stream()
                .map(answer -> modelMapper.map(answer, AnswerResponse.class))
                .toList();
        QuestionResponse questionResponse = modelMapper.map(question, QuestionResponse.class);
        questionResponse.setAnswers(answerResponses);
        return questionResponse;
    }

    @Override
    public List<QuestionResponse> findQuestionsBySessionId(Integer sessionId) {
        List<Question> questions = questionRepository.findBySession_Id(sessionId);
        return questions
                .stream()
                .map(this::convert)
                .toList();
    }

}
