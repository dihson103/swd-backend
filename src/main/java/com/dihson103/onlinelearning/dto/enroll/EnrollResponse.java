package com.dihson103.onlinelearning.dto.enroll;

import com.dihson103.onlinelearning.dto.course.CourseResponse;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EnrollResponse {

    private CourseResponse courseResponse;
    private Double price;
    private LocalDateTime enrollDate;

}
