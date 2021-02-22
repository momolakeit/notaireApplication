package com.momo.notaireApplication.exception;

public class RendezVousNotFoundException extends ObjectNotFoundException {
    private static String NOM_DE_CLASSE = "Rendez vous ";

    public RendezVousNotFoundException() {
        super(NOM_DE_CLASSE);
    }
}
