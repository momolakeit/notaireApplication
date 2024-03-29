package com.momo.notaireApplication.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.momo.notaireApplication.jwt.JwtProvider;
import com.momo.notaireApplication.model.db.Notaire;
import com.momo.notaireApplication.model.db.User;
import com.momo.notaireApplication.model.request.UserSearchQueryDTO;
import com.momo.notaireApplication.repositories.UserRepository;
import com.momo.notaireApplication.testUtils.ObjectTestUtils;
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

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
@ExtendWith(MockitoExtension.class)
class UserControllerTest {
    @Autowired
    private UserController userController;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private User user;

    @Autowired
    private JwtProvider jwtProvider;

    private String JWT_TOKEN;

    @BeforeEach
    public void init() {
        user = ObjectTestUtils.initNotaire();
        user.setId(null);
        user.setEmailAdress("lebron@jamesoui.com");
        user = userRepository.save(user);

        JWT_TOKEN = jwtProvider.generate(user);
    }


    @Test
    public void fetchUserByEmail() throws Exception {
        MockMvc mvc = initMockMvc();
        mvc.perform(MockMvcRequestBuilders.get("/user/email/{email}", user.getEmailAdress())
                .header("Authorization", JWT_TOKEN)
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
                .header("Authorization", JWT_TOKEN)
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

    @Test
    public void fetchUserListByQuery() throws Exception {
        String name="chekevara";
        String lastName="sicily";

        MockMvc mvc = initMockMvc();
        user = ObjectTestUtils.initClient();
        user.setId(null);
        user.setNom(name);
        user.setPrenom(lastName);
        user = userRepository.save(user);
        UserSearchQueryDTO userSearchQueryDTO = new UserSearchQueryDTO();
        userSearchQueryDTO.setQuery(name);
        mvc.perform(MockMvcRequestBuilders.post("/user/search")
                .content(objectMapper.writeValueAsString(userSearchQueryDTO))
                .header("Authorization", JWT_TOKEN)
                .contentType(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    private MockMvc initMockMvc() {
        return MockMvcBuilders.standaloneSetup(userController).build();
    }

}