package com.momo.notaireApplication.controller;

import com.momo.notaireApplication.mapping.ClientMapper;
import com.momo.notaireApplication.model.dto.ClientDTO;
import com.momo.notaireApplication.service.ClientService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/client")
public class ClientController {

    private ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping("getClient/{clientId}")
    public ClientDTO getClient(@PathVariable final Long clientId){
        return this.clientService.findClientDTO(clientId);
    }
}
