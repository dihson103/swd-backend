package com.dihson103.onlinelearning.services;

import com.dihson103.onlinelearning.dto.comment.CommentRequest;
import com.dihson103.onlinelearning.dto.comment.CommentResponse;
import com.dihson103.onlinelearning.dto.comment.UpdateCommentRequest;

import java.util.List;

public interface ICommentService {
    void createComment(String username, CommentRequest commentRequest);

    void updateComment(String username, UpdateCommentRequest updateCommentRequest);

    void deleteCommentAndChildren(String username, Integer commentId);

    List<CommentResponse> getCommentsBySession(Integer sessionId);
}
