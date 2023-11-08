package com.dihson103.onlinelearning.services.impl;

import com.dihson103.onlinelearning.dto.filter.FilterRequestDto;
import com.dihson103.onlinelearning.dto.session.SessionRequest;
import com.dihson103.onlinelearning.dto.session.SessionResponse;
import com.dihson103.onlinelearning.entities.Enroll;
import com.dihson103.onlinelearning.entities.Lesson;
import com.dihson103.onlinelearning.entities.Session;
import com.dihson103.onlinelearning.repositories.EnrollRepository;
import com.dihson103.onlinelearning.repositories.LessonRepository;
import com.dihson103.onlinelearning.repositories.SessionRepository;
import com.dihson103.onlinelearning.repositories.UserRepository;
import com.dihson103.onlinelearning.services.FiltersSpecification;
import com.dihson103.onlinelearning.services.ISessionService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SessionService implements ISessionService {

    private final SessionRepository sessionRepository;
    private final EnrollRepository enrollRepository;
    private final LessonRepository lessonRepository;
    private final ModelMapper modelMapper;
    private final FiltersSpecification<Session> filtersSpecification;

    private boolean isSessionExist(Integer lessonId, String sessionName){
        return sessionRepository.findByLesson_IdAndSessionName(lessonId, sessionName).isPresent();
    }

    @Override
    public void createSession(SessionRequest sessionRequest) {
        if(isSessionExist(sessionRequest.getLessonId(), sessionRequest.getSessionName())){
            throw new IllegalArgumentException("Session is already exist in this lesson.");
        }
        Lesson lesson = lessonRepository.findById(sessionRequest.getLessonId())
                .orElseThrow(() -> new IllegalArgumentException("Can not find lesson has id: " + sessionRequest.getLessonId()));
        Session session = modelMapper.map(sessionRequest, Session.class);
        session.setLesson(lesson);
        sessionRepository.save(session);
    }

    @Override
    public void updateSession(Integer sessionId, SessionRequest sessionRequest) {
        Session session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new IllegalArgumentException("Can not find session has id: " + sessionId));

        if(!sessionRequest.getSessionName().equals(session.getSessionName())){
            if(isSessionExist(sessionRequest.getLessonId(), sessionRequest.getSessionName())){
                throw new IllegalArgumentException("Session is already exist in this lesson.");
            }
        }

        session.setSessionName(sessionRequest.getSessionName());
        session.setVideoAddress(sessionRequest.getVideoAddress());
        session.setStatus(session.getStatus());
        sessionRepository.save(session);
    }

    @Override
    public SessionResponse getSessionActiveById(Integer sessionId, String username) {
        Session session = sessionRepository.findByIdAndStatusIsTrue(sessionId)
                .orElseThrow(() -> new IllegalArgumentException("Can not find session has id: " + sessionId));
        Enroll enroll = enrollRepository.getEnrollByUsernameAndCourse(username, session.getLesson().getCourse().getId())
                .orElseThrow(() -> new IllegalArgumentException("You were not enrolled this course."));
        return modelMapper.map(session, SessionResponse.class);
    }

    @Override
    public List<SessionResponse> getSessionsActiveByLesson(Integer lessonId) {
        List<Session> sessions = sessionRepository.findByLesson_IdAndStatusIsTrue(lessonId);
        return sessions.stream()
                .map(session -> modelMapper.map(session, SessionResponse.class))
                .toList();
    }

    @Override
    public List<SessionResponse> filter(FilterRequestDto filterRequestDto) {
        Specification specification = filtersSpecification.getSpecification(
                filterRequestDto.getSearchRequestDto(),
                filterRequestDto.getGlobalOperator()
        );
        List<Session> sessions = sessionRepository.findAll(specification);
        return sessions.stream().
                map(session -> modelMapper.map(session, SessionResponse.class))
                .toList();
    }
}
