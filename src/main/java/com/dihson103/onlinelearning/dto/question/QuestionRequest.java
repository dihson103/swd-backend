package com.dihson103.onlinelearning.dto.question;

import com.dihson103.onlinelearning.dto.answer.AnswerRequest;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QuestionRequest {

    @NotNull(message = "Question should not be null.")
    private String question;

    @NotNull(message = "Question's explain should not be null.")
//    @NotBlank(message = "Question's explain should not be blank.")
    private String explain;

    @NotNull(message = "Answer list should not null")
    private List<AnswerRequest> answers;

    @NotNull(message = "Session's id should not be null")
    @Min(value = 1, message = "Session's id should more than 0.")
    private Integer sessionId;
}
