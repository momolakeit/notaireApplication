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
import java.util.*;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;


@ExtendWith(MockitoExtension.class)
public class RendezVousServiceTest {
    @InjectMocks
    private RendezVousService rendezVousService;

    @Mock
    private UserService userService;

    @Mock
    private RendezVousRepository rendezVousRepository;

    private final int DUREE_EN_MINUTE = 30;

    @Test
    public void createRendezVousTest() {
        Client client = ObjectTestUtils.initClient();
        Notaire notaire = ObjectTestUtils.initNotaire();
        Long millisecond = System.currentTimeMillis();

        Mockito.when(userService.getUser(1L)).thenReturn(ObjectTestUtils.initClient());
        Mockito.when(userService.getUser(2L)).thenReturn(ObjectTestUtils.initNotaire());
        Mockito.when(rendezVousRepository.save(any(RendezVous.class))).thenAnswer(invocation -> invocation.getArgument(0));

        RendezVous rendezVous = rendezVousService.createRendezVous(1L, 2L, millisecond, DUREE_EN_MINUTE);

        assertValues(client, notaire, millisecond, rendezVous);
    }

    @Test
    public void createRendezVousMemePlageHoraireTest() {
        Client client = ObjectTestUtils.initClient();
        client.setRendezVous(new ArrayList<>(Collections.singletonList(initRendezVousPlageHorairePlusMinutes(0,DUREE_EN_MINUTE))));
        Long millisecond = System.currentTimeMillis();

        Mockito.when(userService.getUser(1L)).thenReturn(client);
        Mockito.when(userService.getUser(2L)).thenReturn(ObjectTestUtils.initNotaire());
        Assertions.assertThrows(PlageHoraireRendezVousException.class, () -> {
            rendezVousService.createRendezVous(1L, 2L, millisecond, DUREE_EN_MINUTE);
        });
    }

    @Test
    public void createRendezVousPlageHoraireJusteApresTest() {
        Client client = ObjectTestUtils.initClient();
        client.setRendezVous(new ArrayList<>(Collections.singletonList(initRendezVousPlageHorairePlusMinutes(31,DUREE_EN_MINUTE))));
        Notaire notaire = ObjectTestUtils.initNotaire();
        Long millisecond = System.currentTimeMillis();

        Mockito.when(userService.getUser(1L)).thenReturn(client);
        Mockito.when(userService.getUser(2L)).thenReturn(ObjectTestUtils.initNotaire());
        Mockito.when(rendezVousRepository.save(any(RendezVous.class))).thenAnswer(invocation -> invocation.getArgument(0));
        RendezVous rendezVous = rendezVousService.createRendezVous(1L, 2L, millisecond, DUREE_EN_MINUTE);
        assertValues(client, notaire, millisecond, rendezVous);
    }

    @Test
    public void createRendezVousPlageHoraireCommenceEtTermineEnMMTmpsTest() {
        Client client = ObjectTestUtils.initClient();
        client.setRendezVous(new ArrayList<>(Collections.singletonList(initRendezVousPlageHorairePlusMinutes(0,DUREE_EN_MINUTE))));
        Long millisecond = System.currentTimeMillis();

        Mockito.when(userService.getUser(1L)).thenReturn(client);
        Mockito.when(userService.getUser(2L)).thenReturn(ObjectTestUtils.initNotaire());
        Assertions.assertThrows(PlageHoraireRendezVousException.class,()->{
            rendezVousService.createRendezVous(1L, 2L, millisecond, DUREE_EN_MINUTE);
        });
    }

    @Test
    public void createRendezVousPlageHoraireCommenceAvantEtTerminePendantQueLeNouveauxCommence() {
        Client client = ObjectTestUtils.initClient();
        Long millisecond = System.currentTimeMillis();
        RendezVous rendezVous = initRendezVousPlageHorairePlusMinutes(-15,DUREE_EN_MINUTE);
        millisecond = millisecond + TimeUnit.MINUTES.toMillis(40);
        LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(millisecond), ZoneId.systemDefault());
        rendezVous.setDateFin(localDateTime);

        client.setRendezVous(new ArrayList<>(Collections.singletonList(rendezVous)));
        Mockito.when(userService.getUser(1L)).thenReturn(client);
        Mockito.when(userService.getUser(2L)).thenReturn(ObjectTestUtils.initNotaire());
        Assertions.assertThrows(PlageHoraireRendezVousException.class, () -> {
            rendezVousService.createRendezVous(1L, 2L, System.currentTimeMillis(), DUREE_EN_MINUTE);
        });

    }

    @Test
    public void createRendezVousPlageHoraireJusteAvantTest() {
        Client client = ObjectTestUtils.initClient();
        client.setRendezVous(new ArrayList<>(Collections.singletonList(initRendezVousPlageHorairePlusMinutes(29,DUREE_EN_MINUTE))));
        Long millisecond = System.currentTimeMillis();

        Mockito.when(userService.getUser(1L)).thenReturn(client);
        Mockito.when(userService.getUser(2L)).thenReturn(ObjectTestUtils.initNotaire());
        Assertions.assertThrows(PlageHoraireRendezVousException.class, () -> {
            rendezVousService.createRendezVous(1L, 2L, millisecond, DUREE_EN_MINUTE);
        });
    }

    @Test
    public void createRendezVousPlageHoraireHierTest() {
        Client client = ObjectTestUtils.initClient();
        client.setRendezVous(new ArrayList<>(Collections.singletonList(initRendezVousPlageHier())));
        Notaire notaire = ObjectTestUtils.initNotaire();
        Long millisecond = System.currentTimeMillis();

        Mockito.when(userService.getUser(1L)).thenReturn(client);
        Mockito.when(userService.getUser(2L)).thenReturn(ObjectTestUtils.initNotaire());
        Mockito.when(rendezVousRepository.save(any(RendezVous.class))).thenAnswer(invocation -> invocation.getArgument(0));
        RendezVous rendezVous = rendezVousService.createRendezVous(1L, 2L, millisecond, DUREE_EN_MINUTE);
        assertValues(client, notaire, millisecond, rendezVous);
    }
    @Test
    public void createRendezVousPlageHoraireOverTest() {
        Client client = ObjectTestUtils.initClient();
        client.setRendezVous(new ArrayList<>(Collections.singletonList(initRendezVousPlageHorairePlusMinutes(-120,120))));
        Long millisecond = System.currentTimeMillis();

        Mockito.when(userService.getUser(1L)).thenReturn(client);
        Mockito.when(userService.getUser(2L)).thenReturn(ObjectTestUtils.initNotaire());
        Assertions.assertThrows(PlageHoraireRendezVousException.class,()->{
            rendezVousService.createRendezVous(1L, 2L, millisecond, DUREE_EN_MINUTE);
        });
    }

    @Test
    public void createRendezVousPlageHoraireDemainTest() {
        Client client = ObjectTestUtils.initClient();
        client.setRendezVous(new ArrayList<>(Collections.singletonList(initRendezVousPlageDemain())));
        Notaire notaire = ObjectTestUtils.initNotaire();
        Long millisecond = System.currentTimeMillis();

        Mockito.when(userService.getUser(1L)).thenReturn(client);
        Mockito.when(userService.getUser(2L)).thenReturn(ObjectTestUtils.initNotaire());
        Mockito.when(rendezVousRepository.save(any(RendezVous.class))).thenAnswer(invocation -> invocation.getArgument(0));
        RendezVous rendezVous = rendezVousService.createRendezVous(1L, 2L, millisecond, DUREE_EN_MINUTE);
        assertValues(client, notaire, millisecond, rendezVous);
    }

    @Test
    public void fetchRendezVous() {
        Mockito.when(rendezVousRepository.findById(anyLong())).thenReturn(Optional.of(initRendezVousPlageHorairePlusMinutes(0,DUREE_EN_MINUTE)));
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

    @Test
    public void fetchAllRendezVous(){
        Mockito.when(rendezVousRepository.findByUsers_Id(anyLong())).thenReturn(Arrays.asList(initRendezVousPlageDemain(),initRendezVousPlageHier()));
        List<RendezVous> rendezVousList =rendezVousService.fetchAllRendezVousForUser(1L);
        assertEquals(2,rendezVousList.size());
    }

    private void assertValues(Client client, Notaire notaire, Long millisecond, RendezVous rendezVous) {
        LocalDateTime dateDebut =
                LocalDateTime.ofInstant(Instant.ofEpochMilli(millisecond), ZoneId.systemDefault());

        Long tempsAjouterMillis = TimeUnit.MINUTES.toMillis(DUREE_EN_MINUTE);
        LocalDateTime dateFin =
                LocalDateTime.ofInstant(Instant.ofEpochMilli(millisecond + tempsAjouterMillis), ZoneId.systemDefault());

        assertEquals(dateDebut, rendezVous.getDateDebut());
        assertEquals(rendezVous.getDateFin(), dateFin);

        Notaire notaireResult = ObjectTestUtils.findNotaireInList(rendezVous.getUsers());
        Client clientResult = ObjectTestUtils.findClientInList(rendezVous.getUsers());

        assertEquals(getLastRendezVousInList(notaireResult.getRendezVous()), rendezVous);
        assertEquals(getLastRendezVousInList(clientResult.getRendezVous()), rendezVous);

        ObjectTestUtils.assertUsers(notaire.getNom(), notaire.getPrenom(), notaire.getEmailAdress(), notaireResult);
        ObjectTestUtils.assertUsers(client.getNom(), client.getPrenom(), client.getEmailAdress(), clientResult);
    }


    private RendezVous initRendezVousPlageHorairePlusMinutes(int minutesDebut,int minutesFin) {
        RendezVous rendezVous = new RendezVous();
        LocalDateTime localDateTime = initLocalDateTimeToCurrentMinutes();
        rendezVous.setDateDebut(localDateTime.plusMinutes(minutesDebut));
        rendezVous.setDateFin(localDateTime.plusMinutes(minutesFin));
        return rendezVous;
    }

    private LocalDateTime initLocalDateTimeToCurrentMinutes() {
        long millisecond = System.currentTimeMillis();
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(millisecond), ZoneId.systemDefault());
    }

    private RendezVous initRendezVousPlageHier() {
        RendezVous rendezVous = new RendezVous();
        LocalDateTime localDateTime = initLocalDateTimeToCurrentMinutes();
        rendezVous.setDateDebut(localDateTime.minusDays(1));
        rendezVous.setDateFin(localDateTime.minusDays(1));
        return rendezVous;
    }

    private RendezVous initRendezVousPlageDemain() {
        RendezVous rendezVous = new RendezVous();
        LocalDateTime localDateTime = initLocalDateTimeToCurrentMinutes();
        rendezVous.setDateDebut(localDateTime.plusDays(1));
        return rendezVous;
    }


    private RendezVous getLastRendezVousInList(List<RendezVous> rendezVous) {
        return rendezVous.get(rendezVous.size() - 1);
    }
}