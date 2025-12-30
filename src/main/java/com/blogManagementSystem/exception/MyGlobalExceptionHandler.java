package com.blogManagementSystem.exception;

import io.jsonwebtoken.ExpiredJwtException;
import org.apache.tomcat.util.http.fileupload.InvalidFileNameException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailSendException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

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

    @ExceptionHandler(InvalidFileNameException.class)
    private ResponseEntity<APIExceptionResponse>myInvalidFileNameException(InvalidFileNameException e){
        APIExceptionResponse exception = new APIExceptionResponse(HttpStatus.NOT_ACCEPTABLE.value(),
                "Invalid Filed Name", e.getMessage());
        return new ResponseEntity<>(exception, HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<APIExceptionResponse>myMethodArgumentNotValidException(MethodArgumentNotValidException e){
        Map<String, String> errorMap = new HashMap<>();

        e.getBindingResult().getFieldErrors().forEach(err->{
            String filedName = err.getField();
            String message = err.getDefaultMessage();
            errorMap.put(filedName, message);
        });

        APIExceptionResponse exception = new APIExceptionResponse(HttpStatus.BAD_REQUEST.value(),
                "Given value/values are invalid.", errorMap);

        return new ResponseEntity<>(exception, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(GenericException.class)
    public ResponseEntity<APIExceptionResponse> myGenericException(GenericException e) {
        APIExceptionResponse exception = new APIExceptionResponse(HttpStatus.UNAUTHORIZED.value(),
                "Something went wrong.", e.getMessage());
        return new ResponseEntity<>(exception, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<APIExceptionResponse> myIllegalArgumentException(IllegalArgumentException e){
        APIExceptionResponse exception = new APIExceptionResponse(HttpStatus.NOT_ACCEPTABLE.value(),
                "Illegal Argument", e.getMessage());

        return new ResponseEntity<>(exception, HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<APIExceptionResponse> myBadCredentialsException(BadCredentialsException e){
        APIExceptionResponse exception = new APIExceptionResponse(HttpStatus.BAD_REQUEST.value(),
                "Bad Credentials", e.getMessage());

        return new ResponseEntity<>(exception, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<APIExceptionResponse> myExpiredJwtException(ExpiredJwtException e){
        APIExceptionResponse exceptionResponse = new APIExceptionResponse(HttpStatus.REQUEST_TIMEOUT.value(),
                "Session Expired.", e.getMessage());

        return new ResponseEntity<>(exceptionResponse, HttpStatus.REQUEST_TIMEOUT);
    }

   @ExceptionHandler(EmailVerificationTokenException.class)
    public ResponseEntity<APIExceptionResponse> myEmailVerificationTokenExceptionHandler(EmailVerificationTokenException e){
        APIExceptionResponse exceptionResponse = new APIExceptionResponse(HttpStatus.BAD_REQUEST.value(),
                "Verification Error", e.getMessage());

        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
   }

   @ExceptionHandler(MailSendException.class)
    public ResponseEntity<APIExceptionResponse> myMailSendException(MailSendException e){
        APIExceptionResponse exceptionResponse = new APIExceptionResponse(HttpStatus.BAD_REQUEST.value(),
                "Invalid Email Address", e.getMessage());

        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
   }


//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<APIExceptionResponse> myGenericExceptionHandler(Exception e){
//        APIExceptionResponse exception = new APIExceptionResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(),
//                "Something Went Wrong", e.getMessage());
//        return new ResponseEntity<>(exception, HttpStatus.BAD_REQUEST);
//    }


}
