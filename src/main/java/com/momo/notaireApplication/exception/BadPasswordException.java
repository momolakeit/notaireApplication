package com.momo.notaireApplication.exception;

public class BadPasswordException extends RuntimeException {
    private static String EXCEPTION_MESSAGE="The password don't match the one we have in store !";

    public BadPasswordException (){
        super(EXCEPTION_MESSAGE);
    }
}
