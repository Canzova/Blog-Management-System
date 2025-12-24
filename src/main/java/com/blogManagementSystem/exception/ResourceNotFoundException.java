package com.blogManagementSystem.exception;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter

@NoArgsConstructor
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String resourceName1, Long resourceId1) {
        super(resourceName1 + " not found with id " + resourceId1);
    }

    public ResourceNotFoundException(String resourceName1, Long resourceId1, String resourceName2, Long resourceId2) {
        super(resourceName1 + " not found with id " + resourceId1 + " for " + resourceName2 + " with id : " + resourceId2);
    }

    public ResourceNotFoundException(String message){
        super(message);
    }

}

