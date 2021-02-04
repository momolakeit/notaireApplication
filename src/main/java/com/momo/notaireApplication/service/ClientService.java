package com.momo.notaireApplication.service;

import com.momo.notaireApplication.model.db.Client;
import com.momo.notaireApplication.model.db.FichierDocument;
import com.momo.notaireApplication.repository.ClientRepository;
import com.momo.notaireApplication.utils.ListUtil;
import org.springframework.stereotype.Service;

@Service
public class ClientService {
    private ClientRepository clientRepository;

    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    //todo lancer erreur 400 et gerer
    public Client findClient(Long id) {
        return this.clientRepository.findById(id).orElse(null);
    }

    public Client ajouterFichierDocument(Client client, FichierDocument fichierDocument) {
        client.setFichierDocuments(ListUtil.initList(client.getFichierDocuments()));
        client.getFichierDocuments().add(fichierDocument);
        return client;
    }

    public Client saveClient(Client client) {
        return this.clientRepository.save(client);
    }
}
