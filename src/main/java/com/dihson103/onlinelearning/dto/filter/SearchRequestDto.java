package com.dihson103.onlinelearning.dto.filter;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchRequestDto {

    private String column;
    private String value;
    private Operator operator;
    private String joinTable;

}
