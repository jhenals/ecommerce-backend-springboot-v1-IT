package com.progetto.ecommercebackend.support.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionControllerAdvice {
    @ExceptionHandler(value = CustomException.class)
    public final ResponseEntity<String> handleCustomException(CustomException exception){
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }

 //   @ExceptionHandler(value = BookNotFoundException.class)
 //   public final ResponseEntity<String> handleCustomException(BookNotFoundException exception){
 //       return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
 //   }
}
