package com.momo.notaireApplication.controller;

import com.momo.notaireApplication.model.db.Notaire;
import com.momo.notaireApplication.repository.NotaireRepository;
import com.momo.notaireApplication.service.NotaireService;
import com.momo.notaireApplication.service.NotaireServiceTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
class NotaireControllerTest {
    @Autowired
    private NotaireController notaireController;

    @Autowired
    private NotaireRepository notaireRepository;
    private Notaire notaire;

    @BeforeEach
    public void init() {
        notaire = new Notaire();
        notaire.setEmailAdress("email@mail.com");
        notaireRepository.save(notaire);
    }
    @Test
    public void fetchNotaire() throws Exception {
        MockMvc mvc = initMockMvc();
        mvc.perform(MockMvcRequestBuilders.get("/notaire/getNotaire/{notaireId}",notaire.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void fetchNotFoundNotaireBadRequest() throws Exception {
        MockMvc mvc = initMockMvc();
        mvc.perform(MockMvcRequestBuilders.get("/notaire/getNotaire/{notaireId}",0)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
    @Test
    public void fetchNotaireByEmail() throws Exception {
        MockMvc mvc = initMockMvc();
        mvc.perform(MockMvcRequestBuilders.get("/notaire/getNotaireByEmail/{notaireEmail}",notaire.getEmailAdress())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }


    private MockMvc initMockMvc() {
        return MockMvcBuilders.standaloneSetup(notaireController).build();
    }

}