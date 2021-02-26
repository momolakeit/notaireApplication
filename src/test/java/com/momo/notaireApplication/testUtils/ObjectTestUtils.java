package com.momo.notaireApplication.testUtils;

import com.momo.notaireApplication.model.db.Client;
import com.momo.notaireApplication.model.db.Notaire;
import com.momo.notaireApplication.model.db.User;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ObjectTestUtils {
    private static final String NOM = "nom";

    private static final String PRENOM = "prenom";

    private static final String EMAIL = "email";
    public static Notaire findNotaireInList(List<User> users){
        return (Notaire) users
                .stream()
                .filter(user -> user instanceof Notaire)
                .findFirst()
                .get();
    }
    public static Client findClientInList(List<User> users){
        return (Client) users
                .stream()
                .filter(user -> user instanceof Client)
                .findFirst()
                .get();
    }

    public static Notaire initNotaire() {
        Notaire notaire = new Notaire();
        notaire.setId(1L);
        notaire.setNom(NOM);
        notaire.setPrenom(PRENOM);
        notaire.setEmailAdress(EMAIL);
        notaire.setRendezVous(new ArrayList<>());
        return notaire;
    }

    public static Client initClient() {
        Client client = new Client();
        client.setId(1L);
        client.setNom(NOM);
        client.setPrenom(PRENOM);
        client.setEmailAdress(EMAIL);
        client.setRendezVous(new ArrayList<>());
        return client;
    }

    public static void assertUsers(String nom , String prenom , String email,User userToAssert){
        assertEquals(nom, userToAssert.getNom());
        assertEquals(email, userToAssert.getEmailAdress());
        assertEquals(prenom, userToAssert.getPrenom());
    }
}
