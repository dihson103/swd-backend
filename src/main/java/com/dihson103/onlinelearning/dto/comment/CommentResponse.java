package com.dihson103.onlinelearning.dto.comment;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentResponse {

    private Integer id;
    private String content;
    private String username;
    private LocalDateTime createDate;
    private List<CommentResponse> childrenComments;

}
