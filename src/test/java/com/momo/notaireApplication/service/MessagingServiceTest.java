package com.momo.notaireApplication.service;

import com.momo.notaireApplication.exception.validation.notFound.ConversationNotFoundException;
import com.momo.notaireApplication.mapping.UserMapper;
import com.momo.notaireApplication.model.db.*;
import com.momo.notaireApplication.model.dto.ConversationDTO;
import com.momo.notaireApplication.model.dto.MessagesDTO;
import com.momo.notaireApplication.model.dto.UserDTO;
import com.momo.notaireApplication.repositories.ConversationRepository;
import com.momo.notaireApplication.repositories.MessagesRepository;
import com.momo.notaireApplication.testUtils.ObjectTestUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MessagingServiceTest {
    @InjectMocks
    private MessagingService messageService;

    @Mock
    private ConversationRepository conversationRepository;

    @Mock
    private MessagesRepository messagesRepository;

    @Mock
    private UserService userService;

    @Mock
    private RendezVousService rendezVousService;

    @Captor
    ArgumentCaptor<Conversation> conversationArgumentCaptor;

    @Captor
    ArgumentCaptor<Messages> messagesArgumentCaptor;

    @Captor
    ArgumentCaptor<List<User>> userListArgumentCaptor;

    @Captor
    ArgumentCaptor<RendezVous> rendezVousArgumentCaptor;

    private final String MESSAGE_DEFAULT = "salut";


    @Test
    public void testCreateConversation() {
        initCreateConversationMock();

        MessagesDTO messagesDTO = ObjectTestUtils.initMessageDTO();

        ConversationDTO conversationDTO = ObjectTestUtils.conversationDTO(Arrays.asList(initClient(), initNotaire()));
        Conversation conversation = messageService.createConversation(conversationDTO, messagesDTO,ObjectTestUtils.rendezVousDTO());

        verify(conversationRepository,times(2)).save(conversationArgumentCaptor.capture());
        verify(userService).saveMutlipleUsers(userListArgumentCaptor.capture());
        verify(messagesRepository).save(messagesArgumentCaptor.capture());
        verify(rendezVousService).saveRendezVous(rendezVousArgumentCaptor.capture());

        Conversation conversationCapturedValue = conversationArgumentCaptor.getValue();
        Messages messageCapturedValue = messagesArgumentCaptor.getValue();
        RendezVous rendezVousCapturedValue = rendezVousArgumentCaptor.getValue();

        List<User> userList = userListArgumentCaptor.getValue();
        for (User user : userList) {
            assertEquals(1, user.getConversations().size());
        }
        assertEquals(2, conversationCapturedValue.getUsers().size());
        assertEquals(MESSAGE_DEFAULT, conversation.getMessages().get(0).getMessage());
        assertEquals(conversation,rendezVousCapturedValue.getConversation());
        assertEquals(conversation.getRendezVous(),rendezVousCapturedValue);
        ObjectTestUtils.assertUsers(messageCapturedValue.getUser().getNom(),
                messageCapturedValue.getUser().getPrenom(),
                messageCapturedValue.getUser().getEmailAdress(),
                ObjectTestUtils.initClient());


    }

    @Test
    public void testAddMessage() {
        when(conversationRepository.findById(anyLong())).thenReturn(Optional.of(new Conversation()));
        when(conversationRepository.save(any(Conversation.class))).thenAnswer(invocationOnMock -> invocationOnMock.getArgument(0));
        when(messagesRepository.save(any(Messages.class))).thenAnswer(invocationOnMock -> invocationOnMock.getArgument(0));

        Conversation conversation = messageService.addMessage(1L, ObjectTestUtils.initMessageDTO());
        assertEquals(1, conversation.getMessages().size());
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
        Assertions.assertThrows(ConversationNotFoundException.class, () -> {
            messageService.getConversation(1L);
        });
    }


    private void initCreateConversationMock() {
        when(conversationRepository.save(any(Conversation.class))).thenAnswer(invocationOnMock -> invocationOnMock.getArgument(0));
        when(messagesRepository.save(any(Messages.class))).thenAnswer(invocationOnMock -> invocationOnMock.getArgument(0));
        when(userService.saveMutlipleUsers(anyList())).thenAnswer(invocationOnMock -> invocationOnMock.getArgument(0));
        when(rendezVousService.getRendezVous(anyLong())).thenReturn(new RendezVous());
        when(rendezVousService.saveRendezVous(any(RendezVous.class))).thenAnswer(invocationOnMock -> invocationOnMock.getArgument(0));

        when(userService.getUser(1L)).thenReturn(initClient());
        when(userService.getUser(2L)).thenReturn(initNotaire());
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