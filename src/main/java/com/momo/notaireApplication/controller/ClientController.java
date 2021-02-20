package com.momo.notaireApplication.controller;

import com.momo.notaireApplication.exception.ObjectNotFoundException;
import com.momo.notaireApplication.mapping.ClientMapper;
import com.momo.notaireApplication.model.dto.ClientDTO;
import com.momo.notaireApplication.service.ClientService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

@RestController
@RequestMapping("/client")
public class ClientController extends BaseController{

    private ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping("getClient/{clientId}")
    public ClientDTO getClient(@PathVariable final Long clientId){
        return this.clientService.findClientDTO(clientId);
    }
}
