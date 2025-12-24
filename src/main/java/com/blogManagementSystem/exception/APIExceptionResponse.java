package com.blogManagementSystem.exception;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Map;

@Getter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class APIExceptionResponse {

    private int errorCode;
    private String error;
    private String message;
    private Map<String, String>messageList;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;

    public APIExceptionResponse(int errorCode, String error, String message) {
        this.errorCode = errorCode;
        this.error = error;
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }

    public APIExceptionResponse(int errorCode, String error, Map<String, String> errorMap) {
        this.errorCode = errorCode;
        this.error = error;
        this.messageList = errorMap;
        this.timestamp = LocalDateTime.now();
    }
}
