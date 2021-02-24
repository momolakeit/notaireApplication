package com.momo.notaireApplication.testUtils;

import com.momo.notaireApplication.model.db.Client;
import com.momo.notaireApplication.model.db.Notaire;
import com.momo.notaireApplication.model.db.User;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ObjectTestUtils {

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
    public static void assertUsers(User user,User userToAssert){
        assertEquals(user.getNom(), userToAssert.getNom());
        assertEquals(user.getEmailAdress(), userToAssert.getEmailAdress());
        assertEquals(user.getPrenom(), userToAssert.getPrenom());
    }
}
