package com.momo.notaireApplication.service;

import com.momo.notaireApplication.mapping.UserMapper;
import com.momo.notaireApplication.model.db.Client;
import com.momo.notaireApplication.model.db.Conversation;
import com.momo.notaireApplication.model.db.Notaire;
import com.momo.notaireApplication.model.db.User;
import com.momo.notaireApplication.model.dto.ConversationDTO;
import com.momo.notaireApplication.model.dto.MessagesDTO;
import com.momo.notaireApplication.model.dto.UserDTO;
import com.momo.notaireApplication.repositories.ConversationRepository;
import com.momo.notaireApplication.testUtils.ObjectTestUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MessagingServiceTest {
    @InjectMocks
    private MessagingService messageService;

    @Mock
    private ConversationRepository conversationRepository;

    @Mock
    private UserService userService;

    @Captor
    ArgumentCaptor<Conversation> conversationArgumentCaptor;

    @Captor
    ArgumentCaptor<List<User>> userListArgumentCaptor;

    @Test
    public void testCreateConversation() {
        initCreateConversationMock();
        List<UserDTO> users = Stream.of(initClient(), initNotaire())
                .map(UserMapper.instance::toDTO)
                .collect(Collectors.toList());

        MessagesDTO messagesDTO = new MessagesDTO();
        messagesDTO.setUser(UserMapper.instance.toDTO(initClient()));
        messagesDTO.setMessage("salut");

        ConversationDTO conversationDTO = new ConversationDTO();
        conversationDTO.setUsers(users);
        conversationDTO = messageService.createConversation(conversationDTO, messagesDTO);

        verify(conversationRepository).save(conversationArgumentCaptor.capture());
        Conversation conversation = conversationArgumentCaptor.getValue();

        verify(userService).saveMutlipleUsers(userListArgumentCaptor.capture());
        List<User> userList = userListArgumentCaptor.getValue();
        for(User user: userList){
            assertEquals(1,user.getConversations().size());
        }
        assertEquals(2,conversation.getUsers().size());
        assertEquals("salut", conversationDTO.getMessages().get(0).getMessage());

    }

    private void initCreateConversationMock() {
        when(conversationRepository.save(any(Conversation.class))).thenAnswer(invocationOnMock -> invocationOnMock.getArgument(0));
        when(userService.saveMutlipleUsers(anyList())).thenAnswer(invocationOnMock -> invocationOnMock.getArgument(0));
        Client client = initClient();
        Notaire notaire = initNotaire();
        when(userService.getUser(1L)).thenReturn(client);
        when(userService.getUser(2L)).thenReturn(notaire);
    }

    private Notaire initNotaire() {
        Notaire notaire = ObjectTestUtils.initNotaire();
        notaire.setId(2L);
        return notaire;
    }

    private Client initClient() {
        Client client = ObjectTestUtils.initClient();
        client.setId(1L);
        return client;
    }

}