package com.dihson103.onlinelearning.dto.answer;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AnswerRequest {

    @NotNull(message = "Answer should not be null.")
    private String answer;

    @NotNull(message = "IsTrueAnswer should not be null.")
    private Boolean isTrueAnswer;

}
