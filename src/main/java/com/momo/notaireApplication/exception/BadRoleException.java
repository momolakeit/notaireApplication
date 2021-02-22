package com.momo.notaireApplication.exception;

public class BadRoleException extends RuntimeException {
    private static String EXCEPTION_MESSAGE="You're passing a role that dont exists";

    public BadRoleException(){
        super(EXCEPTION_MESSAGE);
    }
}
