package com.momo.notaireApplication.service;

import com.momo.notaireApplication.model.db.Client;
import com.momo.notaireApplication.model.db.Facture;
import com.momo.notaireApplication.model.db.Notaire;
import com.momo.notaireApplication.repository.FactureRepository;
import com.momo.notaireApplication.service.payment.StripeService;
import com.momo.notaireApplication.utils.ListUtil;
import com.stripe.exception.StripeException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class FactureService {
    private FactureRepository factureRepository;
    private NotaireService notaireService;
    private ClientService clientService;
    private StripeService stripeService;

    public FactureService(FactureRepository factureRepository, NotaireService notaireService, ClientService clientService, StripeService stripeService) {
        this.factureRepository = factureRepository;
        this.notaireService = notaireService;
        this.clientService = clientService;
        this.stripeService = stripeService;
    }

    public Facture createFacture(Notaire notaire, Client client, BigDecimal prix) {
        Facture facture = new Facture();
        facture.setNotaire(notaire);
        facture.setPrix(prix);
        facture.setDateDeCreation(LocalDateTime.now());
        facture.setClient(client);
        facture = saveFacture(facture);
        linkFactureAndItems(facture, notaire, client);
        stripeService.processPayment(facture, notaire);
        return facture;
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
