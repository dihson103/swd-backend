package com.dihson103.onlinelearning.dto.question;

import com.dihson103.onlinelearning.dto.answer.AnswerResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QuestionResponse {

    private Integer id;

    private String question;

    private String explain;

    private List<AnswerResponse> answers;

}
