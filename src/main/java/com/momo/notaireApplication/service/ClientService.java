package com.momo.notaireApplication.service;

import com.momo.notaireApplication.exception.ClientNotFoundException;
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

    public Client findClient(Long id) {
        return this.clientRepository.findById(id).orElseThrow(ClientNotFoundException::new);
    }

    public Client saveClient(Client client) {
        return this.clientRepository.save(client);
    }
}
