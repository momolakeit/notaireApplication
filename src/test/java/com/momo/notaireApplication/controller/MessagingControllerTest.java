package com.momo.notaireApplication.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.momo.notaireApplication.model.db.Client;
import com.momo.notaireApplication.model.db.Conversation;
import com.momo.notaireApplication.model.db.Notaire;
import com.momo.notaireApplication.model.db.User;
import com.momo.notaireApplication.model.dto.MessagesDTO;
import com.momo.notaireApplication.model.request.CreateConversationDTO;
import com.momo.notaireApplication.repositories.ConversationRepository;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
@Transactional
class MessagingControllerTest {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ConversationRepository conversationRepository;

    @Autowired
    private MessagingController messagingController;

    private List<User> users;

    private Conversation conversation;
    @BeforeEach
    public void init() {
        Client client = ObjectTestUtils.initClient();
        client.setId(null);
        Notaire notaire = ObjectTestUtils.initNotaire();
        notaire.setId(null);
        users = new ArrayList<>(Arrays.asList(client, notaire));
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
    public void getConversation() throws Exception {
        MockMvc mvc = initMockMvc();
        mvc.perform(MockMvcRequestBuilders.post("/conversation/getConversation/{conversationID}",conversation.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

    }

    @Test
    public void getConversationNotFound() throws Exception {
        MockMvc mvc = initMockMvc();
        mvc.perform(MockMvcRequestBuilders.post("/conversation/getConversation/{conversationID}",14444L)
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
        return MockMvcBuilders.standaloneSetup(messagingController).build();
    }
}