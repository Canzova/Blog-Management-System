package com.blogManagementSystem.exception;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class APIExceptionResponse {

    private int errorCode;
    private String error;
    private String message;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;

    public APIExceptionResponse(int errorCode, String error, String message) {
        this.errorCode = errorCode;
        this.error = error;
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }
}
