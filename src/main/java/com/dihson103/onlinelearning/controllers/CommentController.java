package com.dihson103.onlinelearning.controllers;

import com.dihson103.onlinelearning.dto.comment.CommentRequest;
import com.dihson103.onlinelearning.dto.comment.CommentResponse;
import com.dihson103.onlinelearning.dto.comment.UpdateCommentRequest;
import com.dihson103.onlinelearning.dto.common.ApiResponse;
import com.dihson103.onlinelearning.services.ICommentService;
import com.dihson103.onlinelearning.utils.AuthUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/comments")
public class CommentController {

    private final ICommentService service;

    @PostMapping
    @ResponseStatus(CREATED)
    public ApiResponse createComment(@Valid @RequestBody CommentRequest commentRequest){
        String username = AuthUtils.getUserNameLoggedIn();
        service.createComment(username, commentRequest);
        return ApiResponse.builder()
                .message("Create comment success.")
                .build();
    }

    @PutMapping()
    @ResponseStatus(OK)
    public ApiResponse updateComment(@Valid @RequestBody UpdateCommentRequest updateCommentRequest){
        String username = AuthUtils.getUserNameLoggedIn();
        service.updateComment(username, updateCommentRequest);
        return ApiResponse.builder()
                .message("Update comment has id: " + updateCommentRequest.getCommentId() + " success")
                .build();
    }

    @DeleteMapping("{comment-id}")
    public ApiResponse deleteComment(@PathVariable("comment-id") Integer commentId){
        String username = AuthUtils.getUserNameLoggedIn();
        service.deleteCommentAndChildren(username, commentId);
        return ApiResponse.builder()
                .message("Delete comment has id: " + commentId + " success")
                .build();
    }

    @GetMapping("{session-id}")
    @ResponseStatus(OK)
    public ApiResponse<List<CommentResponse>> getCommentsBySession(@PathVariable("session-id") Integer sessionId){
        List<CommentResponse> commentResponses = service.getCommentsBySession(sessionId);
        return ApiResponse.<List<CommentResponse>>builder()
                .message("Get comments of session has id: " + sessionId + " success")
                .data(commentResponses)
                .build();
    }
}
