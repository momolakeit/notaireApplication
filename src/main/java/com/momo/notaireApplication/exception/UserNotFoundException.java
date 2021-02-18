package com.momo.notaireApplication.exception;

public class UserNotFoundException extends ObjectNotFoundException {

    public UserNotFoundException(String typeUtilisateur) {
        super(typeUtilisateur);
    }
}
