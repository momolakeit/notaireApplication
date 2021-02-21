package com.momo.notaireApplication.exception;

public class BadPasswordException extends RuntimeException {
    private static String EXCEPTION_MESSAGE;

    public BadPasswordException (){
        super(EXCEPTION_MESSAGE);
    }
}
