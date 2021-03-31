package com.momo.notaireApplication.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.momo.notaireApplication.model.db.Client;
import com.momo.notaireApplication.model.db.Conversation;
import com.momo.notaireApplication.model.db.Notaire;
import com.momo.notaireApplication.model.db.User;
import com.momo.notaireApplication.model.dto.ConversationDTO;
import com.momo.notaireApplication.model.dto.MessagesDTO;
import com.momo.notaireApplication.model.request.CreateConversationDTO;
import com.momo.notaireApplication.repositories.ConversationRepository;
import com.momo.notaireApplication.repositories.UserRepository;
import com.momo.notaireApplication.testUtils.ObjectTestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class ConversationControllerTest {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ConversationRepository conversationRepository;

    @Autowired
    private ConversationController conversationController;

    private List<User> users;

    private Conversation conversation;
    @BeforeEach
    public void init() {
        Client client = ObjectTestUtils.initClient();
        client.setId(null);
        Notaire notaire = ObjectTestUtils.initNotaire();
        notaire.setId(null);
        users = Arrays.asList(client, notaire);
        userRepository.saveAll(users);
        conversation = new Conversation();
        conversation.setUsers(users);
        conversation = conversationRepository.save(conversation);
    }

    @Test
    public void createConversation() throws Exception {
        MockMvc mvc = initMockMvc();
        MessagesDTO messagesDTO = getMessagesDTOWithProperClientId(ObjectTestUtils.findClientInList(users));

        CreateConversationDTO createConversationDTO = new CreateConversationDTO(ObjectTestUtils.conversationDTO(users), messagesDTO);

        mvc.perform(MockMvcRequestBuilders.post("/conversation/")
                .content(new ObjectMapper().writeValueAsString(createConversationDTO))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void addMessage() throws Exception {
        MockMvc mvc = initMockMvc();
        MessagesDTO messagesDTO = getMessagesDTOWithProperClientId(ObjectTestUtils.findClientInList(users));
        mvc.perform(MockMvcRequestBuilders.post("/conversation/addMessage/{conversationID}",conversation.getId())
                .content(new ObjectMapper().writeValueAsString(messagesDTO))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

    }
    @Test
    public void addMessageConversationNotFound() throws Exception {
        MockMvc mvc = initMockMvc();
        MessagesDTO messagesDTO = getMessagesDTOWithProperClientId(ObjectTestUtils.findClientInList(users));
        mvc.perform(MockMvcRequestBuilders.post("/conversation/addMessage/{conversationID}",14444L)
                .content(new ObjectMapper().writeValueAsString(messagesDTO))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

    }

    private MessagesDTO getMessagesDTOWithProperClientId(Client client) {
        MessagesDTO messagesDTO = ObjectTestUtils.initMessageDTO();
        messagesDTO.getUser().setId(client.getId());
        return messagesDTO;
    }

    private MockMvc initMockMvc() {
        return MockMvcBuilders.standaloneSetup(conversationController).build();
    }
}