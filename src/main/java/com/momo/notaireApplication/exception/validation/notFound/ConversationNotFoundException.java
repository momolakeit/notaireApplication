package com.momo.notaireApplication.exception.validation.notFound;

public class ConversationNotFoundException extends ObjectNotFoundException {
    private static String NOM_DE_CLASSE = "Conversation ";

    public ConversationNotFoundException() {
        super(NOM_DE_CLASSE);
    }
}
