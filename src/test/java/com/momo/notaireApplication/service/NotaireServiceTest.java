package com.momo.notaireApplication.service;

import com.momo.notaireApplication.exception.NotaireNotFoundException;
import com.momo.notaireApplication.model.db.FichierDocument;
import com.momo.notaireApplication.model.db.Notaire;
import com.momo.notaireApplication.repositories.NotaireRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;


@ExtendWith(MockitoExtension.class)
public class NotaireServiceTest {
    @InjectMocks
    private NotaireService notaireService;

    @Mock
    private NotaireRepository notaireRepository;

    private static final String NOM = "nom";

    private static final String PRENOM = "prenom";

    private static final String EMAIL = "email";


    @Test
    public void getNotaireLanceExceptionAssert() {
        Mockito.when(notaireRepository.findById(anyLong())).thenReturn(Optional.empty());
        Assertions.assertThrows(NotaireNotFoundException.class, () -> {
            notaireService.getNotaire(Long.valueOf(1));
        });
    }

    @Test
    public void getNotaire() {
        Mockito.when(notaireRepository.findById(anyLong())).thenReturn(Optional.of(initNotaire()));
        Notaire notaire = notaireService.getNotaire(Long.valueOf(1));
        assertEquals(NOM, notaire.getNom());
        assertEquals(PRENOM, notaire.getPrenom());
        assertEquals(EMAIL, notaire.getEmailAdress());
    }

    @Test
    public void getNotaireAvecEmail() {
        Mockito.when(notaireRepository.findByEmailAdress(anyString())).thenReturn(Optional.of(initNotaire()));
        Notaire notaire = notaireService.findNotaireByEmail(EMAIL);
        assertEquals(NOM, notaire.getNom());
        assertEquals(PRENOM, notaire.getPrenom());
        assertEquals(EMAIL, notaire.getEmailAdress());
    }


    @Test
    public void saveNotaire() {
        Notaire notaire = new Notaire();
        Mockito.when(notaireRepository.save(any(Notaire.class))).thenReturn(initNotaire());
        Notaire returnNotaire = notaireService.saveNotaire(notaire);
        assertEquals(NOM, returnNotaire.getNom());
        assertEquals(PRENOM, returnNotaire.getPrenom());
        assertEquals(EMAIL, returnNotaire.getEmailAdress());
    }


    public static Notaire initNotaire() {
        Notaire notaire = new Notaire();
        notaire.setId(1L);
        notaire.setNom(NOM);
        notaire.setPrenom(PRENOM);
        notaire.setEmailAdress(EMAIL);
        return notaire;
    }

    private Notaire initNotaireAvecListeDeFichier() {
        Notaire notaire = initNotaire();
        notaire.setFichierDocuments(new ArrayList<>(Arrays.asList(new FichierDocument())));
        return notaire;
    }

}