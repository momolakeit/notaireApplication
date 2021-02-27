package com.momo.notaireApplication.exception.validation;

public class BadStripeRoleException extends ValidationException {
    private static String EXCEPTION_MESSAGE="Only a notary can create a stripe account";

    public BadStripeRoleException(){
        super(EXCEPTION_MESSAGE);
    }
}
