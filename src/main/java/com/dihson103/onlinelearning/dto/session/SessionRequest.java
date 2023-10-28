package com.dihson103.onlinelearning.dto.session;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SessionRequest {

    @NotNull(message = "Lesson's id should not be null.")
    @Min(value = 1, message = "Lesson's id should more than 0.")
    private Integer lessonId;

    @NotNull(message = "Session's name should not be null.")
    private String sessionName;

    @NotNull(message = "Video address should not be null.")
    private String videoAddress;

    @NotNull(message = "Session's status should not be null.")
    private Boolean status;

}
