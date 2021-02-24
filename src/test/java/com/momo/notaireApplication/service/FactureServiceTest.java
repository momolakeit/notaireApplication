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
    private NotaireService notaireService;

    @Mock
    private ClientService clientService;

    @Mock
    private StripeService stripeService;

    @Mock
    private FactureRepository factureRepository;

    private Client client;

    private Notaire notaire;


    private void initMocks() {
        client = ClientServiceTest.initClient();
        notaire = NotaireServiceTest.initNotaire();
        Mockito.when(factureRepository.save(any(Facture.class))).thenAnswer(invocationOnMock -> invocationOnMock.getArgument(0));
        Mockito.when(clientService.saveClient(any(Client.class))).thenReturn(client);
        Mockito.when(clientService.findClient(anyLong())).thenReturn(client);
        Mockito.when(notaireService.saveNotaire(any(Notaire.class))).thenReturn(notaire);
        Mockito.when(notaireService.getNotaire(anyLong())).thenReturn(notaire);
    }

    @Test
    void createFacture() {
        initMocks();
        Facture facture = factureService.createFacture(notaire.getId(), client.getId(), BigDecimal.valueOf(29.99));

        Notaire notaireResult = ObjectTestUtils.findNotaireInList(facture.getUsers());
        Client clientResult = ObjectTestUtils.findClientInList(facture.getUsers());

        ObjectTestUtils.assertUsers(notaire,notaireResult);
        ObjectTestUtils.assertUsers(client ,clientResult);
        assertEquals(BigDecimal.valueOf(29.99), facture.getPrix());
        assertEquals(LocalDateTime.now().getDayOfYear(), facture.getDateDeCreation().getDayOfYear());
        assertEquals(LocalDateTime.now().getHour(), facture.getDateDeCreation().getHour());
        assertEquals(LocalDateTime.now().getMinute(), facture.getDateDeCreation().getMinute());
        Mockito.verify(notaireService, Mockito.times(1)).saveNotaire(any(Notaire.class));
        Mockito.verify(clientService, Mockito.times(1)).saveClient(any(Client.class));


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