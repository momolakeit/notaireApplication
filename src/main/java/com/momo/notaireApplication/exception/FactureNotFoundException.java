package com.momo.notaireApplication.exception;

public class FactureNotFoundException extends ObjectNotFoundException {
    private static String NOM_DE_CLASSE = "Facture ";

    public FactureNotFoundException() {
        super(NOM_DE_CLASSE);
    }
}
