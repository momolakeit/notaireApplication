package com.momo.notaireApplication.exception;

public class FactureWithoutNotaireException extends RuntimeException {
    private static String EXCEPTION_MESSAGE="The bill has no notaire !";

    public FactureWithoutNotaireException(){
        super(EXCEPTION_MESSAGE);
    }
}
