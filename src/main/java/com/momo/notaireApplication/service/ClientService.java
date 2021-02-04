package com.momo.notaireApplication.service;

import com.momo.notaireApplication.model.db.Client;
import com.momo.notaireApplication.repository.ClientRepository;
import org.springframework.stereotype.Service;

@Service
public class ClientService {
    private ClientRepository clientRepository;

    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }
    //todo lancer erreur 400 et gerer
    public Client findClient(Long id){
        return this.clientRepository.findById(id).orElse(null);
    }
    public Client saveClient(Client client){
        return this.clientRepository.save(client);
    }
}
