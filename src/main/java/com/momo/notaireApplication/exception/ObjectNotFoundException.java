package com.momo.notaireApplication.exception;

public class ObjectNotFoundException extends ValidationException{
    private static String OBJECT_NOT_FOUND_EXCEPTION_MESSAGE = " not found";

    public ObjectNotFoundException (String nomObject){
        super(nomObject + OBJECT_NOT_FOUND_EXCEPTION_MESSAGE);
    };
}
