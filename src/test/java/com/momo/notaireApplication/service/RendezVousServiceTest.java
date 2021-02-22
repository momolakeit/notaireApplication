package com.momo.notaireApplication.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;


@ExtendWith(MockitoExtension.class)
public class RendezVousServiceTest {
    @InjectMocks
    private RendezVousService rendezVousService;

    @Mock
    private ClientService clientService;

    @Mock
    private NotaireService notaireService;

    @Test
    public void createRendezVousTest(){

    }
}