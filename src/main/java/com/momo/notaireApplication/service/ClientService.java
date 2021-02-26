package com.momo.notaireApplication.service;

import com.momo.notaireApplication.exception.validation.notFound.ClientNotFoundException;
import com.momo.notaireApplication.mapping.ClientMapper;
import com.momo.notaireApplication.model.db.Client;
import com.momo.notaireApplication.model.dto.ClientDTO;
import com.momo.notaireApplication.repositories.ClientRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ClientService {
    private ClientRepository clientRepository;

    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public Client findClient(Long id) {
        return this.clientRepository.findById(id).orElseThrow(ClientNotFoundException::new);
    }
    public ClientDTO findClientDTO(Long id) {
        return ClientMapper.instance.toDTO(findClient(id));
    }

    public Client saveClient(Client client) {
        return this.clientRepository.save(client);
    }
}
