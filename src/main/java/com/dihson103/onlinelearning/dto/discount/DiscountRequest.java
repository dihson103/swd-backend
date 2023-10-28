package com.dihson103.onlinelearning.dto.discount;

import jakarta.persistence.Column;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DiscountRequest {

    @NotNull(message = "Course's id should not be null.")
    @Min(value = 1, message = "Course's id should be more than 0")
    private Integer courseId;

    @NotNull(message = "Discount should not be null.")
    @Min(value = 0, message = "Discount's min is 0")
    @Max(value = 100, message = "Discount's max is 100")
    private Double discount;

    @NotNull(message = "Date from should not be null.")
    private LocalDateTime dateFrom;

    @NotNull(message = "Date to should not be null.")
    private LocalDateTime dateTo;

    public Boolean isFromDateValid(){
        return dateFrom.compareTo(LocalDateTime.now()) >= 0;
    }

    public Boolean isDateToValid(){
        return dateTo.compareTo(dateFrom) > 0;
    }
}
