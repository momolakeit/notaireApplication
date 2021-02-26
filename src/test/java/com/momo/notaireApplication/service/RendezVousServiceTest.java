package com.momo.notaireApplication.service;

import com.momo.notaireApplication.exception.validation.PlageHoraireRendezVousException;
import com.momo.notaireApplication.exception.validation.notFound.RendezVousNotFoundException;
import com.momo.notaireApplication.model.db.Client;
import com.momo.notaireApplication.model.db.Notaire;
import com.momo.notaireApplication.model.db.RendezVous;
import com.momo.notaireApplication.repositories.RendezVousRepository;
import com.momo.notaireApplication.testUtils.ObjectTestUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;


@ExtendWith(MockitoExtension.class)
public class RendezVousServiceTest {
    @InjectMocks
    private RendezVousService rendezVousService;

    @Mock
    private ClientService clientService;

    @Mock
    private NotaireService notaireService;

    @Mock
    private RendezVousRepository rendezVousRepository;

    @Test
    public void createRendezVousTest() {
        Client client = ClientServiceTest.initClient();
        Notaire notaire = NotaireServiceTest.initNotaire();
        Long millisecond = System.currentTimeMillis();

        Mockito.when(clientService.findClient(anyLong())).thenReturn(ClientServiceTest.initClient());
        Mockito.when(notaireService.getNotaire(anyLong())).thenReturn(NotaireServiceTest.initNotaire());
        Mockito.when(rendezVousRepository.save(any(RendezVous.class))).thenAnswer(invocation -> invocation.getArgument(0));

        RendezVous rendezVous = rendezVousService.createRendezVous(1L, 2L, millisecond, 30);

        assertValues(client, notaire, millisecond, rendezVous);
    }

    @Test
    public void createRendezVousMemePlageHoraireTest() {
        Client client = ClientServiceTest.initClient();
        client.setRendezVous(new ArrayList<>(Arrays.asList(initRendezVousPlageHorairePlusMinutes(0))));
        Long millisecond = System.currentTimeMillis();

        Mockito.when(clientService.findClient(anyLong())).thenReturn(client);
        Mockito.when(notaireService.getNotaire(anyLong())).thenReturn(NotaireServiceTest.initNotaire());
        Assertions.assertThrows(PlageHoraireRendezVousException.class, () -> {
            rendezVousService.createRendezVous(1L, 2L, millisecond, 30);
        });
    }

    @Test
    public void createRendezVousPlageHoraireJusteApresTest() {
        Client client = ClientServiceTest.initClient();
        client.setRendezVous(new ArrayList<>(Arrays.asList(initRendezVousPlageHorairePlusMinutes(31))));
        Notaire notaire = NotaireServiceTest.initNotaire();
        Long millisecond = System.currentTimeMillis();

        Mockito.when(clientService.findClient(anyLong())).thenReturn(client);
        Mockito.when(notaireService.getNotaire(anyLong())).thenReturn(NotaireServiceTest.initNotaire());
        Mockito.when(rendezVousRepository.save(any(RendezVous.class))).thenAnswer(invocation -> invocation.getArgument(0));
        RendezVous rendezVous = rendezVousService.createRendezVous(1L, 2L, millisecond, 30);
        assertValues(client, notaire, millisecond, rendezVous);
    }

    @Test
    public void createRendezVousPlageHoraireCommenceEtTermineEnMMTmpsTest() {
        Client client = ClientServiceTest.initClient();
        client.setRendezVous(new ArrayList<>(Arrays.asList(initRendezVousPlageHorairePlusMinutes(30))));
        Notaire notaire = NotaireServiceTest.initNotaire();
        Long millisecond = System.currentTimeMillis();

        Mockito.when(clientService.findClient(anyLong())).thenReturn(client);
        Mockito.when(notaireService.getNotaire(anyLong())).thenReturn(NotaireServiceTest.initNotaire());
        Mockito.when(rendezVousRepository.save(any(RendezVous.class))).thenAnswer(invocation -> invocation.getArgument(0));
        RendezVous rendezVous = rendezVousService.createRendezVous(1L, 2L, millisecond, 30);
        assertValues(client, notaire, millisecond, rendezVous);
    }

    @Test
    public void createRendezVousPlageHoraireCommenceAvantEtTerminePendantQueLeNouveauxCommence() {
        Client client = ClientServiceTest.initClient();
        RendezVous rendezVous = initRendezVousPlageHorairePlusMinutes(-15);
        rendezVous.setDureeEnMinute(40);

        client.setRendezVous(new ArrayList<>(Arrays.asList(rendezVous)));
        Long millisecond = System.currentTimeMillis();
        Mockito.when(clientService.findClient(anyLong())).thenReturn(client);
        Mockito.when(notaireService.getNotaire(anyLong())).thenReturn(NotaireServiceTest.initNotaire());
        Assertions.assertThrows(PlageHoraireRendezVousException.class,()->{
            rendezVousService.createRendezVous(1L,2L,millisecond,30);
        });

    }

    @Test
    public void createRendezVousPlageHoraireJusteAvantTest() {
        Client client = ClientServiceTest.initClient();
        client.setRendezVous(new ArrayList<>(Arrays.asList(initRendezVousPlageHorairePlusMinutes(29))));
        Notaire notaire = NotaireServiceTest.initNotaire();
        Long millisecond = System.currentTimeMillis();

        Mockito.when(clientService.findClient(anyLong())).thenReturn(client);
        Mockito.when(notaireService.getNotaire(anyLong())).thenReturn(NotaireServiceTest.initNotaire());
        Assertions.assertThrows(PlageHoraireRendezVousException.class, () -> {
            rendezVousService.createRendezVous(1L, 2L, millisecond, 30);
        });
    }

    @Test
    public void createRendezVousPlageHoraireHierTest() {
        Client client = ClientServiceTest.initClient();
        client.setRendezVous(new ArrayList<>(Arrays.asList(initRendezVousPlageHier())));
        Notaire notaire = NotaireServiceTest.initNotaire();
        Long millisecond = System.currentTimeMillis();

        Mockito.when(clientService.findClient(anyLong())).thenReturn(client);
        Mockito.when(notaireService.getNotaire(anyLong())).thenReturn(NotaireServiceTest.initNotaire());
        Mockito.when(rendezVousRepository.save(any(RendezVous.class))).thenAnswer(invocation -> invocation.getArgument(0));
        RendezVous rendezVous = rendezVousService.createRendezVous(1L, 2L, millisecond, 30);
        assertValues(client, notaire, millisecond, rendezVous);
    }

    @Test
    public void createRendezVousPlageHoraireDemainTest() {
        Client client = ClientServiceTest.initClient();
        client.setRendezVous(new ArrayList<>(Arrays.asList(initRendezVousPlageDemain())));
        Notaire notaire = NotaireServiceTest.initNotaire();
        Long millisecond = System.currentTimeMillis();

        Mockito.when(clientService.findClient(anyLong())).thenReturn(client);
        Mockito.when(notaireService.getNotaire(anyLong())).thenReturn(NotaireServiceTest.initNotaire());
        Mockito.when(rendezVousRepository.save(any(RendezVous.class))).thenAnswer(invocation -> invocation.getArgument(0));
        RendezVous rendezVous = rendezVousService.createRendezVous(1L, 2L, millisecond, 30);
        assertValues(client, notaire, millisecond, rendezVous);
    }

    @Test
    public void fetchRendezVous() {
        Mockito.when(rendezVousRepository.findById(anyLong())).thenReturn(Optional.of(initRendezVousPlageHorairePlusMinutes(0)));
        RendezVous rendezVous = rendezVousService.getRendezVous(1L);
        assertNotNull(rendezVous);
    }

    @Test
    public void fetchRendezVousNotFound() {
        Mockito.when(rendezVousRepository.findById(anyLong())).thenReturn(Optional.empty());
        Assertions.assertThrows(RendezVousNotFoundException.class, () -> {
            rendezVousService.getRendezVous(1L);
        });

    }

    private void assertValues(Client client, Notaire notaire, Long millisecond, RendezVous rendezVous) {
        LocalDateTime dateTime =
                LocalDateTime.ofInstant(Instant.ofEpochMilli(millisecond), ZoneId.systemDefault());

        assertTrue(dateTime.isEqual(rendezVous.getLocalDateTime()));

        Notaire notaireResult = ObjectTestUtils.findNotaireInList(rendezVous.getUsers());
        Client clientResult = ObjectTestUtils.findClientInList(rendezVous.getUsers());

        assertEquals(getLastRendezVousInList(notaireResult.getRendezVous()), rendezVous);
        assertEquals(getLastRendezVousInList(clientResult.getRendezVous()), rendezVous);

        ObjectTestUtils.assertUsers(notaire, notaireResult);
        ObjectTestUtils.assertUsers(client, clientResult);
    }


    private RendezVous initRendezVousPlageHorairePlusMinutes(int minutes) {
        RendezVous rendezVous = new RendezVous();
        Long millisecond = System.currentTimeMillis();
        LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(millisecond), ZoneId.systemDefault());
        rendezVous.setLocalDateTime(localDateTime.plusMinutes(minutes));
        return rendezVous;
    }

    private RendezVous initRendezVousPlageHier() {
        RendezVous rendezVous = new RendezVous();
        Long millisecond = System.currentTimeMillis();
        LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(millisecond), ZoneId.systemDefault());
        rendezVous.setLocalDateTime(localDateTime.minusDays(1));
        return rendezVous;
    }

    private RendezVous initRendezVousPlageDemain() {
        RendezVous rendezVous = new RendezVous();
        Long millisecond = System.currentTimeMillis();
        LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(millisecond), ZoneId.systemDefault());
        rendezVous.setLocalDateTime(localDateTime.plusDays(1));
        return rendezVous;
    }


    private RendezVous getLastRendezVousInList(List<RendezVous> rendezVous) {
        return rendezVous.get(rendezVous.size() - 1);
    }
}