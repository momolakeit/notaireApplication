package com.momo.notaireApplication.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.momo.notaireApplication.model.db.Notaire;
import com.momo.notaireApplication.model.db.User;
import com.momo.notaireApplication.model.request.CreateRendezVousRequestDTO;
import com.momo.notaireApplication.repositories.UserRepository;
import com.momo.notaireApplication.testUtils.ObjectTestUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
class UserControllerTest {
    @Autowired
    private UserController userController;

    @Autowired
    private UserRepository userRepository;

    private User user;

    @BeforeEach
    public void init() {
        user = ObjectTestUtils.initNotaire();
        user.setId(null);
        user = userRepository.save(user);
    }


    @Test
    public void fetchUserByEmail() throws Exception {
        MockMvc mvc = initMockMvc();
        user = new Notaire();
        user.setEmailAdress("lebron@jamesoui.com");
        user = userRepository.save(user);
        mvc.perform(MockMvcRequestBuilders.get("/user/email/{email}", user.getEmailAdress())
                .contentType(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void fetchUserByEmailUnkownUser() throws Exception {
        MockMvc mvc = initMockMvc();
        mvc.perform(MockMvcRequestBuilders.get("/user/email/{email}", "aaaaaaaaaah@tt.com")
                .contentType(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void fetchUserById() throws Exception {
        MockMvc mvc = initMockMvc();
        mvc.perform(MockMvcRequestBuilders.get("/user/{id}", user.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void fetchUserByIdNotFound() throws Exception {
        MockMvc mvc = initMockMvc();
        mvc.perform(MockMvcRequestBuilders.get("/user/{id}", 0)
                .contentType(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    private MockMvc initMockMvc() {
        return MockMvcBuilders.standaloneSetup(userController).build();
    }

}