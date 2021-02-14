package com.momo.notaireApplication.service;

import com.momo.notaireApplication.model.db.Client;
import com.momo.notaireApplication.model.db.Facture;
import com.momo.notaireApplication.model.db.Notaire;
import com.momo.notaireApplication.repository.FactureRepository;
import com.momo.notaireApplication.service.payment.StripeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;

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

    @BeforeEach
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
        Facture facture = factureService.createFacture(notaire.getId(), client.getId(), BigDecimal.valueOf(29.99));

        assertEquals(client, facture.getClient());
        assertEquals(notaire, facture.getNotaire());
        assertEquals(BigDecimal.valueOf(29.99), facture.getPrix());
        assertEquals(LocalDateTime.now().getDayOfYear(), facture.getDateDeCreation().getDayOfYear());
        assertEquals(LocalDateTime.now().getHour(), facture.getDateDeCreation().getHour());
        assertEquals(LocalDateTime.now().getMinute(), facture.getDateDeCreation().getMinute());
        Mockito.verify(notaireService, Mockito.times(1)).saveNotaire(any(Notaire.class));
        Mockito.verify(clientService, Mockito.times(1)).saveClient(any(Client.class));


    }

    public static Facture initFacture() {
        Facture facture = new Facture();
        facture.setPrix(BigDecimal.valueOf(29.99));
        return facture;
    }
}