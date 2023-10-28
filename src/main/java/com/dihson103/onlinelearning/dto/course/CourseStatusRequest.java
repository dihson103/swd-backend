package com.dihson103.onlinelearning.dto.course;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CourseStatusRequest {

    @NotNull(message = "Course's status should not be null.")
    private Integer courseId;

    @NotNull(message = "Course's status should not be null.")
    private Boolean status;

    private List<Integer> lessonIds;

    private List<Integer> sessionIds;

}
