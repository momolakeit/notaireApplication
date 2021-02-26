package com.momo.notaireApplication.exception.validation.notFound;

import com.momo.notaireApplication.exception.validation.notFound.ObjectNotFoundException;

public class FactureNotFoundException extends ObjectNotFoundException {
    private static String NOM_DE_CLASSE = "Facture ";

    public FactureNotFoundException() {
        super(NOM_DE_CLASSE);
    }
}
