package com.dihson103.onlinelearning.controllers;

import com.dihson103.onlinelearning.dto.common.ApiResponse;
import com.dihson103.onlinelearning.dto.question.QuestionRequest;
import com.dihson103.onlinelearning.dto.question.QuestionResponse;
import com.dihson103.onlinelearning.services.IQuestionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/questions")
public class QuestionController {

    private final IQuestionService service;

    @PostMapping
    @ResponseStatus(CREATED)
    @PreAuthorize("hasAnyAuthority('ADMIN', 'ADMIN_COURSE')")
    public ApiResponse createQuestion(@Valid @RequestBody QuestionRequest questionRequest){
        service.createQuestion(questionRequest);
        return ApiResponse.builder()
                .message("Create question success.")
                .build();
    }

    @PutMapping("{question-id}")
    @ResponseStatus(OK)
    @PreAuthorize("hasAnyAuthority('ADMIN', 'ADMIN_COURSE')")
    public ApiResponse updateQuestion(@Valid @RequestBody QuestionRequest questionRequest,
                                      @PathVariable("question-id") Integer questionId){
        service.updateQuestion(questionId, questionRequest);
        return ApiResponse.builder()
                .message("Update question success.")
                .build();
    }

    @GetMapping("{question-id}")
    @ResponseStatus(OK)
    @PreAuthorize("hasAnyAuthority('ADMIN', 'ADMIN_COURSE')")
    public ApiResponse<QuestionResponse> getQuestion(@PathVariable("question-id") Integer questionId){
        QuestionResponse questionResponse = service.findQuestionResponseById(questionId);
        return ApiResponse.<QuestionResponse>builder()
                .message("Get question has id: " + questionId + " success")
                .data(questionResponse)
                .build();
    }

    @GetMapping("{session-id}/session")
    @ResponseStatus(OK)
    @PreAuthorize("hasAnyAuthority('ADMIN', 'ADMIN_COURSE')")
    public ApiResponse<List<QuestionResponse>> getQuestionsBySessionId(@PathVariable("session-id") Integer sessionId){
        List<QuestionResponse> questions = service.findQuestionsBySessionId(sessionId);
        return ApiResponse.<List<QuestionResponse>>builder()
                .message("Get questions of session has id: " + sessionId + " success")
                .data(questions)
                .build();
    }

}
