package com.momo.notaireApplication.service;

import com.momo.notaireApplication.model.db.Client;
import com.momo.notaireApplication.model.db.Facture;
import com.momo.notaireApplication.model.db.Notaire;
import com.momo.notaireApplication.repositories.FactureRepository;
import com.momo.notaireApplication.service.payment.StripeService;
import com.momo.notaireApplication.testUtils.ObjectTestUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

@ExtendWith(MockitoExtension.class)
public class FactureServiceTest {
    @InjectMocks
    private FactureService factureService;

    @Mock
    private UserService userService;

    @Mock
    private FactureRepository factureRepository;

    private Client client;

    private Notaire notaire;


    private void initMocks() {
        client = ObjectTestUtils.initClient();
        notaire = ObjectTestUtils.initNotaire();
        Mockito.when(factureRepository.save(any(Facture.class))).thenAnswer(invocationOnMock -> invocationOnMock.getArgument(0));
        Mockito.when(userService.saveUser(any(Client.class))).thenReturn(client);
        Mockito.when(userService.getUser(1L)).thenReturn(client);
        Mockito.when(userService.saveUser(any(Notaire.class))).thenReturn(notaire);
        Mockito.when(userService.getUser(2L)).thenReturn(notaire);
    }

    @Test
    void createFacture() {
        initMocks();
        client.setId(1L);
        notaire.setId(2L);
        Facture facture = factureService.createFacture(notaire.getId(), client.getId(), BigDecimal.valueOf(29.99));

        Notaire notaireResult = ObjectTestUtils.findNotaireInList(facture.getUsers());
        Client clientResult = ObjectTestUtils.findClientInList(facture.getUsers());

        ObjectTestUtils.assertUsers(notaire.getNom(),notaire.getPrenom(),notaire.getEmailAdress(),notaireResult);
        ObjectTestUtils.assertUsers(client.getNom(),client.getPrenom(),client.getEmailAdress() ,clientResult);
        assertEquals(BigDecimal.valueOf(29.99), facture.getPrix());
        assertEquals(LocalDateTime.now().getDayOfYear(), facture.getDateDeCreation().getDayOfYear());
        assertEquals(LocalDateTime.now().getHour(), facture.getDateDeCreation().getHour());
        assertEquals(LocalDateTime.now().getMinute(), facture.getDateDeCreation().getMinute());
        Mockito.verify(userService, Mockito.times(1)).saveUser(any(Notaire.class));
        Mockito.verify(userService, Mockito.times(1)).saveUser(any(Client.class));


    }
    @Test
    void getFacture() {
        Mockito.when(factureRepository.findById(anyLong())).thenReturn(Optional.of(initFacture()));
        Facture facture = factureService.getFacture(1L);
        assertEquals(BigDecimal.valueOf(29.99), facture.getPrix());
        assertEquals(LocalDateTime.now().getDayOfYear(), facture.getDateDeCreation().getDayOfYear());
        assertEquals(LocalDateTime.now().getHour(), facture.getDateDeCreation().getHour());
        assertEquals(LocalDateTime.now().getMinute(), facture.getDateDeCreation().getMinute());

    }


    public static Facture initFacture() {
        Facture facture = new Facture();
        facture.setDateDeCreation(LocalDateTime.now());
        facture.setPrix(BigDecimal.valueOf(29.99));
        return facture;
    }
}