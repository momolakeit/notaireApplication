package com.momo.notaireApplication.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.momo.notaireApplication.model.db.Client;
import com.momo.notaireApplication.model.db.Notaire;
import com.momo.notaireApplication.model.db.RendezVous;
import com.momo.notaireApplication.model.db.User;
import com.momo.notaireApplication.model.request.CreateRendezVousRequestDTO;
import com.momo.notaireApplication.repositories.ClientRepository;
import com.momo.notaireApplication.repositories.RendezVousRepository;
import com.momo.notaireApplication.repositories.UserRepository;
import com.sun.codemodel.JStatement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
public class RendezVousControllerTest {

    @Autowired
    private RendezVousController rendezVousController;

    @Autowired
    private RendezVousRepository rendezVousRepository;

    @Autowired
    private UserRepository userRepository;

    private RendezVous rendezVous;

    private Client client;

    private Notaire notaire;

    @BeforeEach
    public void init() {
        RendezVous rendezVous = new RendezVous();
        rendezVous.setLocalDateTime( LocalDateTime.ofInstant(Instant.ofEpochMilli(System.currentTimeMillis()), ZoneId.systemDefault()));


        client = userRepository.save(new Client());
        notaire = userRepository.save(new Notaire());
        rendezVous = rendezVousRepository.save(new RendezVous());
    }

    @Test
    public void createRendezVous() throws Exception {
        CreateRendezVousRequestDTO createRendezVousRequestDTO = initRequestDTO();
        MockMvc mvc = initMockMvc();
        mvc.perform(MockMvcRequestBuilders.post("/rendezVous/")
                    .content(new ObjectMapper().writeValueAsString(createRendezVousRequestDTO))
                    .contentType(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());
    }

    private CreateRendezVousRequestDTO initRequestDTO() {
        CreateRendezVousRequestDTO createRendezVousRequestDTO = new CreateRendezVousRequestDTO();
        createRendezVousRequestDTO.setClientId(client.getId());
        createRendezVousRequestDTO.setNotaireId(notaire.getId());
        createRendezVousRequestDTO.setDate(System.currentTimeMillis());
        createRendezVousRequestDTO.setDureeEnMinute(30);
        return createRendezVousRequestDTO;
    }

   /* private void initUsers(User user,int minutes){
        RendezVous rendezVous = new RendezVous();
        rendezVous.setLocalDateTime();
        userRepository.save(user);
    }*/

    private MockMvc initMockMvc() {
        return MockMvcBuilders.standaloneSetup(rendezVousController).build();
    }

}