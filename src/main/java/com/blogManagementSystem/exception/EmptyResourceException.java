package com.blogManagementSystem.exception;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class EmptyResourceException extends RuntimeException{

    public EmptyResourceException(String resourceName){
        super(resourceName + " is empty.");
    }

}
