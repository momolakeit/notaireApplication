package com.momo.notaireApplication.testUtils;

import com.momo.notaireApplication.exception.FactureNotFoundException;
import com.momo.notaireApplication.model.db.Client;
import com.momo.notaireApplication.model.db.Facture;
import com.momo.notaireApplication.model.db.Notaire;
import com.momo.notaireApplication.model.db.User;

import java.util.List;

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
}
