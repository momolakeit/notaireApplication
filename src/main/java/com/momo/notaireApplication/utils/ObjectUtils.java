package com.momo.notaireApplication.utils;

import com.momo.notaireApplication.exception.FactureWithoutNotaireException;
import com.momo.notaireApplication.model.db.Client;
import com.momo.notaireApplication.model.db.Notaire;
import com.momo.notaireApplication.model.db.User;

import java.util.List;

public class ObjectUtils {

    public static Notaire findNotaireInList(List<User> users){
        return (Notaire) users
                .stream()
                .filter(user -> user instanceof Notaire)
                .findFirst()
                .orElseThrow(FactureWithoutNotaireException::new);

    }
}
