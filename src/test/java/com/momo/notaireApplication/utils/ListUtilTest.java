package com.momo.notaireApplication.utils;

import com.momo.notaireApplication.model.db.Notaire;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(MockitoExtension.class)
class ListUtilTest {

    @Test
    void initListNonNullRetourneMemeListe() {
        List<Notaire> notaires = new ArrayList<>();
        assertEquals(notaires,ListUtil.initList(notaires));
    }
    @Test
    void initListNullRetourneNouvelleListe() {
        List<Notaire> notaires =null;
        List<Notaire> returnListe =ListUtil.initList(notaires);
        assertNotEquals(notaires,returnListe);
        assertNotNull(returnListe);
    }

    @Test
    void ajouterObjectAListe() {
        List<Notaire> notaires =new ArrayList<>();
        List<Notaire> returnList = ListUtil.ajouterObjectAListe(new Notaire(),notaires);
        assertEquals(1,returnList.size());
    }
    @Test
    void ajouterObjectAListeAvecData() {
        List<Notaire> notaires =new ArrayList<>(Arrays.asList(new Notaire()));
        List<Notaire> returnList = ListUtil.ajouterObjectAListe(new Notaire(),notaires);
        assertEquals(2,returnList.size());
    }
}