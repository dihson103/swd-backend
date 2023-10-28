package com.dihson103.onlinelearning.dto.common;

import lombok.Builder;

@Builder
public record ErrorResponse(
        String error
) {
}
