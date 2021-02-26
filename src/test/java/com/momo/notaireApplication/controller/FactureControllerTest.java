package com.momo.notaireApplication.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.momo.notaireApplication.mapping.ClientMapper;
import com.momo.notaireApplication.mapping.NotaireMapper;
import com.momo.notaireApplication.model.db.Client;
import com.momo.notaireApplication.model.db.Notaire;
import com.momo.notaireApplication.model.request.CreateFactureRequestDTO;
import com.momo.notaireApplication.repositories.ClientRepository;
import com.momo.notaireApplication.repositories.NotaireRepository;
import com.momo.notaireApplication.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class FactureControllerTest {
    @Autowired
    private FactureController factureController;

    @Autowired
    private UserRepository userRepository;

    private Client client;

    private Notaire notaire;

    @BeforeEach
    public void init() {
        client = new Client();
        client = userRepository.save(client);
        notaire = new Notaire();
        notaire = userRepository.save(notaire);
    }

    @Test
    public void testCreateFacture() throws Exception {
        MockMvc mvc = initMockMvc();
        CreateFactureRequestDTO factureRequestDTO = new CreateFactureRequestDTO();
        factureRequestDTO.setClientDTO(ClientMapper.instance.toDTO(client));
        factureRequestDTO.setNotaireDTO(NotaireMapper.instance.toDTO(notaire));
        factureRequestDTO.setPrix(BigDecimal.valueOf(55));
        mvc.perform(MockMvcRequestBuilders.post("/facture/")
                .content(new ObjectMapper().writeValueAsString(factureRequestDTO))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    private MockMvc initMockMvc() {
        return MockMvcBuilders.standaloneSetup(factureController).build();
    }
}