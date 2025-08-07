package com.auth.api.exception.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@Builder
public class StandardErrorDTO {

    private Instant timestamp;
    private Integer status;
    private String error;
    private String message;
    private String path;
}
