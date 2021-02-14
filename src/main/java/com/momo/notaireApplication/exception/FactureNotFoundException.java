package com.momo.notaireApplication.exception;

public class FactureNotFoundException extends RuntimeException {
    private static String FACTURE_NOT_FOUND_EXCEPTION_MESSAGE = "Facture not found";

    public FactureNotFoundException() {
        super(FACTURE_NOT_FOUND_EXCEPTION_MESSAGE);
    }
}
