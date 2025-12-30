package com.blogManagementSystem.exception;

public class EmailVerificationTokenException extends RuntimeException{
    public EmailVerificationTokenException(String message){
        super(message);
    }

}
