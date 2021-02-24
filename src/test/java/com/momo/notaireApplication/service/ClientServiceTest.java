package com.momo.notaireApplication.service;

import com.momo.notaireApplication.exception.ClientNotFoundException;
import com.momo.notaireApplication.model.db.Client;
import com.momo.notaireApplication.model.db.FichierDocument;
import com.momo.notaireApplication.repositories.ClientRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

@ExtendWith(MockitoExtension.class)
class ClientServiceTest {
    @InjectMocks
    private ClientService clientService;

    @Mock
    private ClientRepository clientRepository;

    private static final String NOM = "nom";

    private static final String PRENOM = "prenom";

    private static final String EMAIL = "email";


    @Test
    public void getClientLanceExceptionAssert() {
        Mockito.when(clientRepository.findById(anyLong())).thenReturn(Optional.empty());
        Assertions.assertThrows(ClientNotFoundException.class, () -> {
            clientService.findClient(Long.valueOf(1));
        });
    }

    @Test
    public void getClient() {
        Mockito.when(clientRepository.findById(anyLong())).thenReturn(Optional.of(initClient()));
        Client Client = clientService.findClient(Long.valueOf(1));
        assertEquals(NOM, Client.getNom());
        assertEquals(PRENOM, Client.getPrenom());
        assertEquals(EMAIL, Client.getEmailAdress());
    }

    @Test
    public void saveClient() {
        Client client = new Client();
        Mockito.when(clientRepository.save(any(Client.class))).thenReturn(initClient());
        Client returnClient = clientService.saveClient(client);
        assertEquals(NOM, returnClient.getNom());
        assertEquals(PRENOM, returnClient.getPrenom());
        assertEquals(EMAIL, returnClient.getEmailAdress());
    }

    public static Client initClient() {
        Client client = new Client();
        client.setId(1L);
        client.setNom(NOM);
        client.setPrenom(PRENOM);
        client.setEmailAdress(EMAIL);
        client.setRendezVous(new ArrayList<>());
        return client;
    }

    private Client initClientAvecListeDeFichier() {
        Client Client = initClient();
        Client.setFichierDocuments(new ArrayList<>(Arrays.asList(new FichierDocument())));
        return Client;
    }
}