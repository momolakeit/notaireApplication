package com.momo.notaireApplication.service;

import com.momo.notaireApplication.exception.PlageHoraireRendezVousException;
import com.momo.notaireApplication.exception.RendezVousNotFoundException;
import com.momo.notaireApplication.model.db.Client;
import com.momo.notaireApplication.model.db.Notaire;
import com.momo.notaireApplication.model.db.RendezVous;
import com.momo.notaireApplication.repositories.RendezVousRepository;
import com.momo.notaireApplication.utils.ListUtil;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RendezVousService {
    private RendezVousRepository rendezVousRepository;

    private ClientService clientService;

    private NotaireService notaireService;

    public RendezVousService(RendezVousRepository rendezVousRepository, ClientService clientService, NotaireService notaireService) {
        this.rendezVousRepository = rendezVousRepository;
        this.clientService = clientService;
        this.notaireService = notaireService;
    }

    public RendezVous createRendezVous(Long clientId, Long notaireId, Long date, int dureeEnMinute) {
        Client client = this.clientService.findClient(clientId);
        Notaire notaire = this.notaireService.getNotaire(notaireId);
        LocalDateTime dateTime =
                LocalDateTime.ofInstant(Instant.ofEpochMilli(date), ZoneId.systemDefault());
        if (checkIfRendezVousLibre(dureeEnMinute, client, notaire, dateTime)) {
            RendezVous rendezVous = initRendezVous(client, notaire, dateTime, dureeEnMinute);
            this.linkRendezVousAndItems(client, notaire, rendezVous);
            return rendezVous;
        } else {
            throw new PlageHoraireRendezVousException();
        }

    }

    public RendezVous getRendezVous(Long id) {
        return rendezVousRepository.findById(id).orElseThrow(RendezVousNotFoundException::new);
    }

    public RendezVous saveRendezVous(RendezVous rendezVous) {
        return this.rendezVousRepository.save(rendezVous);
    }

    private void linkRendezVousAndItems(Client client, Notaire notaire, RendezVous rendezVous) {
        this.linkRendezVousAndClient(client, rendezVous);
        this.linkRendezVousAndNotaire(notaire, rendezVous);
    }

    private void linkRendezVousAndClient(Client client, RendezVous rendezVous) {
        ListUtil.ajouterObjectAListe(rendezVous, client.getRendezVous());
        this.clientService.saveClient(client);
    }

    private RendezVous initRendezVous(Client client, Notaire notaire, LocalDateTime dateTime, int dureeEnMinute) {
        RendezVous rendezVous = new RendezVous();
        rendezVous.setLocalDateTime(dateTime);
        rendezVous.setDureeEnMinute(dureeEnMinute);
        rendezVous.setUsers(new ArrayList<>(Arrays.asList(client, notaire)));
        rendezVous = this.saveRendezVous(rendezVous);
        return rendezVous;
    }

    private void linkRendezVousAndNotaire(Notaire notaire, RendezVous rendezVous) {
        ListUtil.ajouterObjectAListe(rendezVous, notaire.getRendezVous());
        this.notaireService.saveNotaire(notaire);
    }

    private Boolean checkIfRendezVousLibre(List<RendezVous> rendezVous, LocalDateTime dateRendezVous, int dureeEnMinute) {
        List heureDesRndezVousQuonPiettine = rendezVous.stream()
                .map(RendezVous::getLocalDateTime)
                .filter(date -> ifRendezVousSameDay(date, dateRendezVous) &&
                        ifPlageHoraireLibre(date, dateRendezVous, dureeEnMinute))
                .collect(Collectors.toList());
        if (heureDesRndezVousQuonPiettine.isEmpty()) {
            return true;
        } else {
            return false;
        }
    }

    private Boolean ifPlageHoraireLibre(LocalDateTime date, LocalDateTime dateRendezVous, int dureeEnMinute) {
        return dateRendezVous.isBefore(date) &&
                dateRendezVous.isAfter(dateRendezVous.plusMinutes(dureeEnMinute));
    }

    private Boolean ifRendezVousSameDay(LocalDateTime date, LocalDateTime dateRendezVous) {
        return date.getYear() == dateRendezVous.getYear() &&
                date.getDayOfYear() == dateRendezVous.getDayOfYear();
    }

    private boolean checkIfRendezVousLibre(int dureeEnMinute, Client client, Notaire notaire, LocalDateTime dateTime) {
        return checkIfRendezVousLibre(client.getRendezVous(), dateTime, dureeEnMinute) &&
                checkIfRendezVousLibre(notaire.getRendezVous(), dateTime, dureeEnMinute);
    }
}
