package com.dihson103.onlinelearning.dto.comment;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CommentRequest {

    private Integer sessionId;
    private Integer parentCommentId;

    @NotNull(message = "Comment should not be null here.")
    private String content;

}
