package com.momo.notaireApplication.exception;

public class FichierDocumentNotFoundException extends RuntimeException {
    private static String FICHIER_DOCUMENT_NOT_FOUND_EXCEPTION_MESSAGE = "Fichier document not found";

    public FichierDocumentNotFoundException() {
        super(FICHIER_DOCUMENT_NOT_FOUND_EXCEPTION_MESSAGE);
    }
}
