package com.dihson103.onlinelearning.dto.lesson;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LessonRequest {

    @NotBlank(message = "Lesson's title should not be null.")
    private String title;

    @NotBlank(message = "Lesson's content should not be null.")
    private String content;

    @NotNull(message = "Course's id should not be null.")
    @Min(value = 1, message = "Course's id should more than 0")
    private Integer courseId;

    @NotNull(message = "Lesson's status should not be null.")
    private Boolean status;
}
