package com.momo.notaireApplication.service;

import com.momo.notaireApplication.exception.validation.PlageHoraireRendezVousException;
import com.momo.notaireApplication.exception.validation.notFound.RendezVousNotFoundException;
import com.momo.notaireApplication.mapping.RendezVousMapper;
import com.momo.notaireApplication.model.db.Client;
import com.momo.notaireApplication.model.db.Notaire;
import com.momo.notaireApplication.model.db.RendezVous;
import com.momo.notaireApplication.model.db.User;
import com.momo.notaireApplication.model.dto.RendezVousDTO;
import com.momo.notaireApplication.repositories.RendezVousRepository;
import com.momo.notaireApplication.utils.ListUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class RendezVousService {
    private RendezVousRepository rendezVousRepository;
    private UserService userService;


    public RendezVousService(RendezVousRepository rendezVousRepository, UserService userService) {
        this.rendezVousRepository = rendezVousRepository;
        this.userService = userService;
    }

    public RendezVous createRendezVous(Long clientId, Long notaireId, Long date, int dureeEnMinute) {
        Client client = (Client) userService.getUser(clientId);
        Notaire notaire = (Notaire) userService.getUser(notaireId);
        LocalDateTime dateTime = getDateTimeFromMillis(date);
        if (checkIfRendezVousLibre(dureeEnMinute, client, notaire, dateTime)) {
            RendezVous rendezVous = initRendezVous(client, notaire, dateTime, dureeEnMinute);
            linkRendezVousAndItems(client, notaire, rendezVous);
            return rendezVous;
        } else {
            throw new PlageHoraireRendezVousException();
        }

    }

    public RendezVous getRendezVous(Long id) {
        return rendezVousRepository.findById(id).orElseThrow(RendezVousNotFoundException::new);
    }

    public RendezVousDTO toDTO(RendezVous rendezVous) {
        return RendezVousMapper.instance.toDTO(rendezVous);
    }

    public RendezVous saveRendezVous(RendezVous rendezVous) {
        return this.rendezVousRepository.save(rendezVous);
    }

    private void linkRendezVousAndItems(Client client, Notaire notaire, RendezVous rendezVous) {
        linkRendezVousAndUser(client, rendezVous);
        linkRendezVousAndUser(notaire, rendezVous);
    }

    private void linkRendezVousAndUser(User user, RendezVous rendezVous) {
        ListUtil.ajouterObjectAListe(rendezVous, user.getRendezVous());
        userService.saveUser(user);
    }

    private RendezVous initRendezVous(Client client, Notaire notaire, LocalDateTime dateTime, int dureeEnMinute) {
        RendezVous rendezVous = new RendezVous();
        rendezVous.setLocalDateTime(dateTime);
        rendezVous.setDureeEnMinute(dureeEnMinute);
        rendezVous.setUsers(new ArrayList<>(Arrays.asList(client, notaire)));
        rendezVous = this.saveRendezVous(rendezVous);
        return rendezVous;
    }

    private Boolean ifPlageHoraireLibre(LocalDateTime date, LocalDateTime dateRendezVous, int dureeEnMinuteAncienRendezVous, int dureeEnMinuteNouveauRendezVous) {
        //retirer nanos set seconde pour pouvoir faire comparaison
        date = setLocalDateWithoutNanoOrSeconds(date);
        dateRendezVous = setLocalDateWithoutNanoOrSeconds(dateRendezVous);

        return (date.isBefore(dateRendezVous) &&
                date.plusMinutes(dureeEnMinuteAncienRendezVous).isBefore(dateRendezVous)) ||
                date.isAfter(dateRendezVous.plusMinutes(dureeEnMinuteNouveauRendezVous)) ||
                date.isEqual(dateRendezVous.plusMinutes(dureeEnMinuteNouveauRendezVous));
    }

    private Boolean ifRendezVousSameDay(LocalDateTime date, LocalDateTime dateRendezVous) {
        return date.getYear() == dateRendezVous.getYear() &&
                date.getDayOfYear() == dateRendezVous.getDayOfYear();
    }

    private Boolean checkIfRendezVousLibre(List<RendezVous> rendezVous, LocalDateTime dateRendezVous, int dureeEnMinute) {
        List heureDesRndezVousQuonPiettine = rendezVous.stream()
                //.map(RendezVous::getLocalDateTime)
                .filter(rv -> ifRendezVousSameDay(rv.getLocalDateTime(), dateRendezVous) &&
                        !ifPlageHoraireLibre(rv.getLocalDateTime(), dateRendezVous, rv.getDureeEnMinute(), dureeEnMinute))
                .collect(Collectors.toList());
        return heureDesRndezVousQuonPiettine.isEmpty();
    }

    private boolean checkIfRendezVousLibre(int dureeEnMinute, Client client, Notaire notaire, LocalDateTime dateTime) {
        return checkIfRendezVousLibre(client.getRendezVous(), dateTime, dureeEnMinute) &&
                checkIfRendezVousLibre(notaire.getRendezVous(), dateTime, dureeEnMinute);
    }

    private LocalDateTime getDateTimeFromMillis(Long date) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(date), ZoneId.systemDefault());
    }

    private LocalDateTime setLocalDateWithoutNanoOrSeconds(LocalDateTime date) {
        date = date.withNano(0);
        date = date.withSecond(0);
        return date;
    }
}
