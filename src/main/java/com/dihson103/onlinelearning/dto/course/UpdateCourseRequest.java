package com.dihson103.onlinelearning.dto.course;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

@Getter
@Setter
@Builder

public class UpdateCourseRequest {

    @NotNull(message = "Id should not be null.")
    private Integer id;

    @NotBlank(message = "Course's should not be blank.")
    @NotNull(message = "Course's should not be null.")
    private String courseName;

    @NotNull(message = "Price should not be null.")
    @PositiveOrZero(message = "Price should be a non-negative value.")
    private Double price;

    @NotNull(message = "Course's title should not be null")
    @NotBlank(message = "Course's title should not be blank")
    private String title;

    @NotNull(message = "Course's image should not be null")
    @NotBlank(message = "Course's image should not be blank")
    private String image;
}
