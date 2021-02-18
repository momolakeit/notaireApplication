package com.momo.notaireApplication.controller;

import com.momo.notaireApplication.exception.ObjectNotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

public abstract class BaseController {
    @ExceptionHandler(ObjectNotFoundException.class)
    public ResponseEntity<Object> handleConflict(RuntimeException ex) {
        return new ResponseEntity<Object>(ex.getMessage(),new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }
}
