package com.appliance_store.Exception;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class CustomException extends RuntimeException
{
    @ExceptionHandler(value = RuntimeException.class)
    ResponseEntity<String> RuntimeExceptionHandler(RuntimeException exception)
    {
        return ResponseEntity.badRequest().body(exception.getMessage());
    }
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    ResponseEntity<String> ValidationHandler(MethodArgumentNotValidException exception)
    {
        return ResponseEntity.badRequest().body(exception.getFieldError().getDefaultMessage());
    }
    @ExceptionHandler(value = Exception.class)
    ResponseEntity<String> ExceptionHandler(Exception exception)
    {
        return ResponseEntity.badRequest().body(exception.getMessage());
    }
    @ExceptionHandler(value= AccessDeniedException.class)
    ResponseEntity<String> AccessDenideExceptionHandler(AccessDeniedException exception)
    {
        return ResponseEntity.badRequest().body(exception.getMessage());
    }
}
