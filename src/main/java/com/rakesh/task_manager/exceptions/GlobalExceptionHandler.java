package com.rakesh.task_manager.exceptions;

import com.rakesh.task_manager.dto.payload.MessageResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String,String>> myMethodArgumentNotValidException(MethodArgumentNotValidException exception){
        Map<String, String> response = new HashMap<>();
        exception.getBindingResult()
                .getAllErrors()
                .forEach(
                        objectError -> {
                            String fieldName = ((FieldError) objectError).getField();
                            String defaultMessage = objectError.getDefaultMessage();
                            response.put(fieldName,defaultMessage);
                        }
                );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<MessageResponse> myResourceNotFoundException(ResourceNotFoundException exception){
        MessageResponse response = new MessageResponse();
        response.setMessage(exception.getMessage());
        response.setSuccess(false);
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
}
