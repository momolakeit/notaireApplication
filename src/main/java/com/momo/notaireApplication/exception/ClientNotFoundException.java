package com.momo.notaireApplication.exception;

public class ClientNotFoundException extends UserNotFoundException {
    private static String NOM_DE_CLASSE = "Client";

    public ClientNotFoundException() {
        super(NOM_DE_CLASSE);
    }
}
