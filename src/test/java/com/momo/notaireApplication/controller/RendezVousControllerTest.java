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
import com.momo.notaireApplication.testUtils.ObjectTestUtils;
import com.sun.codemodel.JStatement;
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
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
@ExtendWith(MockitoExtension.class)
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
        rendezVous = new RendezVous();
        rendezVous.setDateDebut(LocalDateTime.ofInstant(Instant.ofEpochMilli(System.currentTimeMillis()), ZoneId.systemDefault()));


        client = userRepository.save(ObjectTestUtils.initClient());
        notaire = userRepository.save(ObjectTestUtils.initNotaire());
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

    @Test
    public void createRendezVousMauvaisePlageHoraireBadRequest() throws Exception {
        client = initClientAvecRendezVousMauvaisePlageHoraire();
        CreateRendezVousRequestDTO createRendezVousRequestDTO = initRequestDTO();
        MockMvc mvc = initMockMvc();
        mvc.perform(MockMvcRequestBuilders.post("/rendezVous/")
                .content(new ObjectMapper().writeValueAsString(createRendezVousRequestDTO))
                .contentType(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void fetchRendezVous() throws Exception {
        MockMvc mvc = initMockMvc();
        mvc.perform(MockMvcRequestBuilders.get("/rendezVous/getRendezVous/{id}", rendezVous.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
    @Test
    public void fetchUsersRendezVous() throws Exception {
        client = initClientAvecRendezVousMauvaisePlageHoraire();
        MockMvc mvc = initMockMvc();
        mvc.perform(MockMvcRequestBuilders.get("/rendezVous/getAllRendezVousForUser/{id}", client.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
    @Test
    public void fetchRendezVousNotFoundBadRequest() throws Exception {
        MockMvc mvc = initMockMvc();
        mvc.perform(MockMvcRequestBuilders.get("/rendezVous/getRendezVous/{id}", 0L)
                .contentType(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }


    private Client initClientAvecRendezVousMauvaisePlageHoraire() {
        client = new Client();
        RendezVous rV = rendezVousRepository.save(initRendezVousAvecMauvaisePlageHoraire());
        client.setRendezVous(new ArrayList<>(Arrays.asList(rV)));
        client =userRepository.save(client);
        rV.setUsers(new ArrayList<>(Arrays.asList(client)));
        rendezVousRepository.save(rV);
        return client;
    }


    private CreateRendezVousRequestDTO initRequestDTO() {
        CreateRendezVousRequestDTO createRendezVousRequestDTO = new CreateRendezVousRequestDTO();
        createRendezVousRequestDTO.setClientId(client.getId());
        createRendezVousRequestDTO.setNotaireId(notaire.getId());
        createRendezVousRequestDTO.setDate(System.currentTimeMillis());
        createRendezVousRequestDTO.setDureeEnMinute(30);
        return createRendezVousRequestDTO;
    }

    private RendezVous initRendezVousAvecMauvaisePlageHoraire() {
        RendezVous rendezVous = new RendezVous();
        LocalDateTime localDateTime =LocalDateTime.ofInstant(Instant.ofEpochMilli(System.currentTimeMillis()), ZoneId.systemDefault());
        rendezVous.setDateDebut(localDateTime);
        rendezVous.setDateFin(localDateTime.plusMinutes(1));
        return rendezVousRepository.save(rendezVous);
    }

    private MockMvc initMockMvc() {
        return MockMvcBuilders.standaloneSetup(rendezVousController).build();
    }

}