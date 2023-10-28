package com.dihson103.onlinelearning.repositories;

import com.dihson103.onlinelearning.entities.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Integer> {

    List<Comment> findBySession_IdAndParentCommentIsNull(Integer sessionId);
}
