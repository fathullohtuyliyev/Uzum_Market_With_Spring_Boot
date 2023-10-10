package com.example.demo.exception;

import org.apache.tomcat.util.http.fileupload.impl.SizeException;
import org.apache.tomcat.util.http.fileupload.impl.SizeLimitExceededException;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.FileNotFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    /*@ExceptionHandler(value = Exception.class)
    public ResponseEntity<Object> handleException(Exception e) {
        return new ResponseEntity<>("Exception has appeared: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }*/
    @ExceptionHandler(value = BadParamException.class)
    public ResponseEntity<Object> handleException(BadParamException e) {
        return new ResponseEntity<>("Bad Request: " + e.getMessage(), HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(value = SizeLimitExceededException.class)
    public ResponseEntity<Object> handleException(SizeLimitExceededException e){
        return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(value = IllegalArgumentException.class)
    public ResponseEntity<Object> handleException(IllegalArgumentException e) {
        return new ResponseEntity<>("Bad Request: " + e.getMessage(), HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(value = ForbiddenAccessException.class)
    public ResponseEntity<Object> handleException(ForbiddenAccessException e) {
        return new ResponseEntity<>("Forbidden Access: " + e.getMessage(), HttpStatus.FORBIDDEN);
    }
    @ExceptionHandler(value = NotFoundException.class)
    public ResponseEntity<Object> handleException(NotFoundException e) {
        return new ResponseEntity<>("Not Found: " + e.getMessage(), HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(value = FileNotFoundException.class)
    public ResponseEntity<Object> handleException(FileNotFoundException e) {
        return new ResponseEntity<>("Not Found: " + e.getMessage(), HttpStatus.NOT_FOUND);
    }
}
