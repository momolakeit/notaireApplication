package com.momo.notaireApplication.exception;

public class UserAlreadyExistsException extends ValidationException {
    private static String ERROR_MESSAGE = "A user with this email already exists";

    public UserAlreadyExistsException() {
        super(ERROR_MESSAGE);
    }
}
