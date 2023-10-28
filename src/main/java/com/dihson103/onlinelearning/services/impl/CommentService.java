package com.dihson103.onlinelearning.services.impl;

import com.dihson103.onlinelearning.dto.comment.CommentRequest;
import com.dihson103.onlinelearning.dto.comment.CommentResponse;
import com.dihson103.onlinelearning.dto.comment.UpdateCommentRequest;
import com.dihson103.onlinelearning.entities.Comment;
import com.dihson103.onlinelearning.entities.Session;
import com.dihson103.onlinelearning.entities.UserEntity;
import com.dihson103.onlinelearning.repositories.CommentRepository;
import com.dihson103.onlinelearning.repositories.SessionRepository;
import com.dihson103.onlinelearning.repositories.UserRepository;
import com.dihson103.onlinelearning.services.ICommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService implements ICommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final SessionRepository sessionRepository;

    @Override
    public void createComment(String username, CommentRequest commentRequest) {
        UserEntity user = userRepository.findByUsernameAndStatusIsTrue(username)
                .orElseThrow(() -> new IllegalArgumentException("Can not find user has username: " + username));
        if(commentRequest.getParentCommentId() != null){
            createCommentToComment(user, commentRequest);
        }
        else if(commentRequest.getSessionId() != null) {
            createCommentToSession(user, commentRequest);
        }
        else {
            throw new IllegalArgumentException("SessionId and commentIdTo should not be null at the same time.");
        }
    }

    @Override
    public void updateComment(String username, UpdateCommentRequest updateCommentRequest) {
        Comment comment = findByIdAndCheckPermission(username, updateCommentRequest.getCommentId());
        comment.setContent(updateCommentRequest.getContent());
        commentRepository.save(comment);
    }

    @Override
    public void deleteCommentAndChildren(String username, Integer commentId) {
        Comment comment = findByIdAndCheckPermission(username, commentId);
        deleteCommentAndChildrenRecursive(comment);
    }

    @Override
    public List<CommentResponse> getCommentsBySession(Integer sessionId) {
        List<Comment> comments = commentRepository.findBySession_IdAndParentCommentIsNull(sessionId);
        return comments
                .stream()
                .map(this::convert)
                .toList();
    }

    private CommentResponse convert(Comment comment){
        return CommentResponse.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .username(comment.getUser().getUsername())
                .createDate(comment.getCreateDate())
                .childrenComments(comment.getChildComments().stream().map(this::convert).toList())
                .build();
    }

    private void deleteCommentAndChildrenRecursive(Comment comment){
        List<Comment> childrenComments = comment.getChildComments();
        if(childrenComments != null){
            childrenComments.stream().forEach(this::deleteCommentAndChildrenRecursive);
        }
        commentRepository.delete(comment);
    }

    private Comment findByIdAndCheckPermission(String username, Integer commentId){
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() ->  new IllegalArgumentException("Can not find comment has id: "+ commentId));
        if(!comment.getUser().getUsername().equals(username)){
            throw new IllegalArgumentException("You do not have permission to update this comment.");
        }
        return comment;
    }

    private void createCommentToSession(UserEntity user, CommentRequest commentRequest) {
        Session session = sessionRepository.findByIdAndStatusIsTrue(commentRequest.getSessionId())
                .orElseThrow(() -> new IllegalArgumentException("Can not find session has id: "+ commentRequest.getSessionId()));
        saveComment(user, commentRequest.getContent(), session, null);
    }

    private void createCommentToComment(UserEntity user, CommentRequest commentRequest) {
        Comment commentTo = commentRepository.findById(commentRequest.getParentCommentId())
                .orElseThrow(() -> new IllegalArgumentException("Can not find comment has id: " + commentRequest.getParentCommentId()));
        saveComment(user, commentRequest.getContent(), commentTo.getSession(), commentTo);
    }

    private void saveComment(UserEntity user, String content, Session session, Comment parentComment){
        Comment comment = Comment.builder()
                .content(content)
                .parentComment(parentComment)
                .session(session)
                .status(true)
                .createDate(LocalDateTime.now())
                .user(user)
                .build();
        commentRepository.save(comment);
    }


}
