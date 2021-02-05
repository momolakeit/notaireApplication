package com.momo.notaireApplication.exception;

public class UserNotFoundException extends RuntimeException {
    private static String USER_NOT_FOUND_EXCEPTION_MESSAGE = " not found";

    public UserNotFoundException(String typeUtisateur) {
        super(typeUtisateur + USER_NOT_FOUND_EXCEPTION_MESSAGE);
    }
}
