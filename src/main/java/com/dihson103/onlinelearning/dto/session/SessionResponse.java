package com.dihson103.onlinelearning.dto.session;

import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SessionResponse {

    private Integer id;
    private String sessionName;
    private String videoAddress;
    private Boolean status;

}
