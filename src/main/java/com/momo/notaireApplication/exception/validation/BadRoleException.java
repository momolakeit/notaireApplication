package com.momo.notaireApplication.exception.validation;

public class BadRoleException extends ValidationException {
    private static String EXCEPTION_MESSAGE="You're passing a role that dont exists";

    public BadRoleException(){
        super(EXCEPTION_MESSAGE);
    }
}
