package com.momo.notaireApplication.service;

import com.momo.notaireApplication.exception.validation.notFound.FactureNotFoundException;
import com.momo.notaireApplication.mapping.FactureMapper;
import com.momo.notaireApplication.model.db.Client;
import com.momo.notaireApplication.model.db.Facture;
import com.momo.notaireApplication.model.db.Notaire;
import com.momo.notaireApplication.model.db.User;
import com.momo.notaireApplication.model.dto.FactureDTO;
import com.momo.notaireApplication.repositories.FactureRepository;
import com.momo.notaireApplication.utils.ListUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;

@Service
@Transactional
public class FactureService {
    private FactureRepository factureRepository;
    private UserService userService;

    public FactureService(FactureRepository factureRepository, UserService userService) {
        this.factureRepository = factureRepository;
        this.userService = userService;
    }

    public Facture createFacture(Long notaireId, Long clientId, BigDecimal prix) {
        Facture facture = new Facture();
        Notaire notaire = (Notaire) userService.getUser(notaireId);
        Client client = (Client) userService.getUser(clientId);
        facture.setUsers(new ArrayList<>(Arrays.asList(client, notaire)));
        facture.setPrix(prix);
        facture.setDateDeCreation(LocalDateTime.now());
        facture = saveFacture(facture);
        linkFactureAndItems(facture, notaire, client);
        return facture;
    }

    public Facture getFacture(Long id) {
        return this.factureRepository.findById(id).orElseThrow(FactureNotFoundException::new);
    }

    public FactureDTO getFactureDTO(Long id) {
        return FactureMapper.instance.toDTO(this.getFacture(id));
    }


    public Facture saveFacture(Facture facture) {
        return factureRepository.save(facture);
    }

    private void linkFactureAndItems(Facture facture, Notaire notaire, Client client) {
        linkFactureAndUser(facture, client);
        linkFactureAndUser(facture, notaire);
    }

    private void linkFactureAndUser(Facture facture, User user) {
        ListUtil.ajouterObjectAListe(facture, user.getFactures());
        userService.saveUser(user);
    }

}
