package com.momo.notaireApplication.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.momo.notaireApplication.model.db.Client;
import com.momo.notaireApplication.model.request.SignUpDTO;
import com.momo.notaireApplication.repositories.ClientRepository;
import com.momo.notaireApplication.service.authentification.AuthServiceTest;
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

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Autowired
    private AuthController authController;

    @Autowired
    private ClientRepository clientRepository;

    private final String EMAIL = "lebron@james.com";

    public final String MAUVAIS_ROLE = "MAUVAIS ROLE";


    @Test
    public void createUser() throws Exception {
        MockMvc mvc = initMockMvc();
        mvc.perform(MockMvcRequestBuilders.post("/user")
                .content(new ObjectMapper().writeValueAsString(AuthServiceTest.initSignUpDTONotaireRole()))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void createUserBadRoleThrowsException() throws Exception {
        MockMvc mvc = initMockMvc();
        SignUpDTO signUpDTO = AuthServiceTest.initSignUpDTONotaireRole();
        signUpDTO.setRole(MAUVAIS_ROLE);
        mvc.perform(MockMvcRequestBuilders.post("/user")
                .content(new ObjectMapper().writeValueAsString(MAUVAIS_ROLE))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void createAlreadyExistsUser() throws Exception {
        initUserInRepo();
        MockMvc mvc = initMockMvc();
        SignUpDTO signUpDTO = AuthServiceTest.initSignUpDTONotaireRole();
        signUpDTO.setEmailAdress(EMAIL);
        mvc.perform(MockMvcRequestBuilders.post("/user")
                .content(new ObjectMapper().writeValueAsString(signUpDTO))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    private void initUserInRepo() {
        Client client = new Client();
        client.setEmailAdress(EMAIL);
        clientRepository.save(client);
    }

    private MockMvc initMockMvc() {
        return MockMvcBuilders.standaloneSetup(authController).build();
    }

}