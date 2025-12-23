package com.blogManagementSystem.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class MyGlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    private ResponseEntity<APIExceptionResponse>myResourceNotFoundExceptionHandler(ResourceNotFoundException e){
        APIExceptionResponse exception = new APIExceptionResponse(HttpStatus.NOT_FOUND.value(),
                "Resource Not Found", e.getMessage());
        return new ResponseEntity<>(exception, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EmptyResourceException.class)
    private ResponseEntity<APIExceptionResponse>myEmptyResourceExceptionHandler(EmptyResourceException e){
        APIExceptionResponse exception = new APIExceptionResponse(HttpStatus.NOT_FOUND.value(),
                "Empty Resource Found", e.getMessage());
        return new ResponseEntity<>(exception, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<APIExceptionResponse> myGenericExceptionHandler(Exception e){
        APIExceptionResponse exception = new APIExceptionResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Something Went Wrong", e.getMessage());
        return new ResponseEntity<>(exception, HttpStatus.BAD_REQUEST);
    }


}
