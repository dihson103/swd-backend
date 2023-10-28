package com.dihson103.onlinelearning.dto.course;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CourseResponse {

    private Integer id;

    private String courseName;

    private Double price;

    private String title;

    private Boolean status;

    private LocalDateTime createdDate;

    private LocalDateTime publicDate;

    private String image;

}
