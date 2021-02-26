package com.momo.notaireApplication.exception.validation;

public class PlageHoraireRendezVousException extends ValidationException {
    private static String EXCEPTION_MESSAGE="The rendez vous cannot be added to calendars";

    public PlageHoraireRendezVousException(){
        super(EXCEPTION_MESSAGE);
    }
}
