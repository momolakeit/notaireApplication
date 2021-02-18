package com.momo.notaireApplication.service;

import com.momo.notaireApplication.exception.FactureNotFoundException;
import com.momo.notaireApplication.mapping.FactureMapper;
import com.momo.notaireApplication.model.db.Client;
import com.momo.notaireApplication.model.db.Facture;
import com.momo.notaireApplication.model.db.Notaire;
import com.momo.notaireApplication.model.dto.FactureDTO;
import com.momo.notaireApplication.repository.FactureRepository;
import com.momo.notaireApplication.utils.ListUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@Transactional
public class FactureService {
    private FactureRepository factureRepository;
    private NotaireService notaireService;
    private ClientService clientService;

    public FactureService(FactureRepository factureRepository, NotaireService notaireService, ClientService clientService) {
        this.factureRepository = factureRepository;
        this.notaireService = notaireService;
        this.clientService = clientService;
    }

    public Facture createFacture(Long notaireId,Long clientId, BigDecimal prix) {
        Facture facture = new Facture();
        Notaire notaire = this.notaireService.getNotaire(notaireId);
        Client client = this.clientService.findClient(clientId);
        facture.setNotaire(notaire);
        facture.setPrix(prix);
        facture.setDateDeCreation(LocalDateTime.now());
        facture.setClient(client);
        facture = saveFacture(facture);
        linkFactureAndItems(facture, notaire, client);
        return facture;
    }

    public Facture getFacture(Long id){
        return this.factureRepository.findById(id).orElseThrow(FactureNotFoundException::new);
    }
    public FactureDTO getFactureDTO(Long id){
        return FactureMapper.instance.toDTO(this.getFacture(id));
    }


    public Facture saveFacture(Facture facture) {
        return factureRepository.save(facture);
    }

    private void linkFactureAndItems(Facture facture, Notaire notaire, Client client) {
        linkFactureAndClient(facture, client);
        linkFactureAndNotaire(facture, notaire);
    }

    private void linkFactureAndNotaire(Facture facture, Notaire notaire) {
        ListUtil.ajouterObjectAListe(facture, notaire.getFactures());
        notaireService.saveNotaire(notaire);
    }

    private void linkFactureAndClient(Facture facture, Client client) {
        ListUtil.ajouterObjectAListe(facture, client.getFactures());
        clientService.saveClient(client);
    }

}
