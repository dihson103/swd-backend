package com.dihson103.onlinelearning.dto.filter;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class FilterRequestDto {

    private List<SearchRequestDto> searchRequestDto;
    private GlobalOperator globalOperator;

}
