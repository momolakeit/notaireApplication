package com.momo.notaireApplication.exception.validation.notFound;

public class NotaireNotFoundException extends UserNotFoundException {
    private static String NOM_DE_CLASSE = "Notaire";

    public NotaireNotFoundException() {
        super(NOM_DE_CLASSE);
    }
}