package com.momo.notaireApplication.service;

import com.momo.notaireApplication.exception.validation.notFound.ConversationNotFoundException;
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
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
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

    private final String MESSAGE_DEFAULT = "salut";

    @Test
    public void testCreateConversation() {
        initCreateConversationMock();

        MessagesDTO messagesDTO = ObjectTestUtils.initMessageDTO();

        ConversationDTO conversationDTO = ObjectTestUtils.conversationDTO(Arrays.asList(initClient(),initNotaire()));
        Conversation conversation = messageService.createConversation(conversationDTO, messagesDTO);

        verify(conversationRepository).save(conversationArgumentCaptor.capture());
        Conversation conversationCapturedValue = conversationArgumentCaptor.getValue();

        verify(userService).saveMutlipleUsers(userListArgumentCaptor.capture());
        List<User> userList = userListArgumentCaptor.getValue();
        for (User user : userList) {
            assertEquals(1, user.getConversations().size());
        }
        assertEquals(2, conversationCapturedValue.getUsers().size());
        assertEquals(MESSAGE_DEFAULT, conversation.getMessages().get(0).getMessage());

    }

    @Test
    public void testAddMessage() {
        when(conversationRepository.findById(anyLong())).thenReturn(Optional.of(new Conversation()));
        when(conversationRepository.save(any(Conversation.class))).thenAnswer(invocationOnMock -> invocationOnMock.getArgument(0));

        Conversation conversation = messageService.addMessage(1L,ObjectTestUtils.initMessageDTO());
        assertEquals(1,conversation.getMessages().size());
        assertEquals(MESSAGE_DEFAULT, conversation.getMessages().get(0).getMessage());
    }

    @Test
    public void testGetMessage() {
        when(conversationRepository.findById(anyLong())).thenReturn(Optional.of(new Conversation()));
        Conversation conversation = messageService.getConversation(1L);
        assertNotNull(conversation);
    }

    @Test
    public void testGetMessageLanceException() {
        when(conversationRepository.findById(anyLong())).thenReturn(Optional.empty());
        Assertions.assertThrows(ConversationNotFoundException.class,()->{
            messageService.getConversation(1L);
        });
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