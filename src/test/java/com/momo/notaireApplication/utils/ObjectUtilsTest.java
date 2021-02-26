package com.momo.notaireApplication.utils;

import com.momo.notaireApplication.exception.validation.FactureWithoutNotaireException;
import com.momo.notaireApplication.model.db.Client;
import com.momo.notaireApplication.model.db.Notaire;
import com.momo.notaireApplication.model.db.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(MockitoExtension.class)
class ObjectUtilsTest {
    @Test
    public void findNotaireInList(){
        List<User> users = new ArrayList<>(Arrays.asList(new Notaire()));
        Notaire notaire = ObjectUtils.findNotaireInList(users);
        assertNotNull(notaire);
    }
    @Test
    public void findNoNotaireInList(){
        List<User> users = new ArrayList<>(Arrays.asList(new Client()));
        Assertions.assertThrows(FactureWithoutNotaireException.class, () -> {
            ObjectUtils.findNotaireInList(users);
        });
    }
}