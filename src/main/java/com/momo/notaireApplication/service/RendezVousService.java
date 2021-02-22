package com.momo.notaireApplication.service;

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

    public RendezVous createRendezVous(Long clientId, Long notaireId, Long date) {
        Client client = this.clientService.findClient(clientId);
        Notaire notaire = this.notaireService.getNotaire(notaireId);
        LocalDateTime dateTime =
                LocalDateTime.ofInstant(Instant.ofEpochMilli(date), ZoneId.systemDefault());
        RendezVous rendezVous = new RendezVous();
        rendezVous.setLocalDateTime(dateTime);
        rendezVous.setClient(client);
        rendezVous.setNotaire(notaire);
        rendezVous = this.saveRendezVous(rendezVous);
        this.linkRendezVousAndItems(client, notaire, rendezVous);
        return rendezVous;

    }
    public RendezVous getRendezVous(Long id){
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

    private void linkRendezVousAndNotaire(Notaire notaire, RendezVous rendezVous) {
        ListUtil.ajouterObjectAListe(rendezVous, notaire.getRendezVous());
        this.notaireService.saveNotaire(notaire);
    }
}
