package com.momo.notaireApplication.exception.validation.notFound;

import com.momo.notaireApplication.exception.validation.notFound.UserNotFoundException;

public class ClientNotFoundException extends UserNotFoundException {
    private static String NOM_DE_CLASSE = "Client";

    public ClientNotFoundException() {
        super(NOM_DE_CLASSE);
    }
}
