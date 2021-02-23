package com.momo.notaireApplication.controller;

import com.momo.notaireApplication.exception.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

public abstract class BaseController {
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<Object> handleValidationException(RuntimeException ex) {
        return new ResponseEntity<Object>(ex.getMessage(),new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(BadPasswordException.class)
    public ResponseEntity<Object> handleBadPasswordException(RuntimeException ex) {
        return new ResponseEntity<Object>(ex.getMessage(),new HttpHeaders(), HttpStatus.UNAUTHORIZED);
    }
}
