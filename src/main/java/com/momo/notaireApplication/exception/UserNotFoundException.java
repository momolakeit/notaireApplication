package com.momo.notaireApplication.exception;

public class UserNotFoundException extends ObjectNotFoundException {

    private static String GENERIC_MESSAGE ="User";

    public UserNotFoundException(String typeUtilisateur) {
        super(typeUtilisateur);
    }
    public UserNotFoundException() {
        super(GENERIC_MESSAGE);
    }
}
