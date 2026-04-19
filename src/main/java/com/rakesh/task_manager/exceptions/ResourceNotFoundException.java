package com.rakesh.task_manager.exceptions;

public class ResourceNotFoundException extends RuntimeException{
    public ResourceNotFoundException(String message){
        super(message);
    }
    public ResourceNotFoundException(String resourcename, Long id){
        super(String.format("%s not found with %",resourcename, id));
    }
}
