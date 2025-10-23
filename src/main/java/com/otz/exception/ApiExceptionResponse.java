package com.otz.exception;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ApiExceptionResponse {
    private LocalDateTime timestamp;
    private int status;
    private String error;
    private String path;
}
