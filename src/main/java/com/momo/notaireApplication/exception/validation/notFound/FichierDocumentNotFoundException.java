package com.momo.notaireApplication.exception.validation.notFound;

import com.momo.notaireApplication.exception.validation.notFound.ObjectNotFoundException;

public class FichierDocumentNotFoundException extends ObjectNotFoundException {
    private static String NOM_DE_CLASSE = "Fichier document";

    public FichierDocumentNotFoundException() {
        super(NOM_DE_CLASSE);
    }
}
