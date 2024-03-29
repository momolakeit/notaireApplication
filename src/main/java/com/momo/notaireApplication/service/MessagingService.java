package com.momo.notaireApplication.service;

import com.momo.notaireApplication.exception.validation.notFound.ConversationNotFoundException;
import com.momo.notaireApplication.mapping.ConversationMapper;
import com.momo.notaireApplication.mapping.MessageMapper;
import com.momo.notaireApplication.model.db.*;
import com.momo.notaireApplication.model.dto.ConversationDTO;
import com.momo.notaireApplication.model.dto.FichierDocumentDTO;
import com.momo.notaireApplication.model.dto.MessagesDTO;
import com.momo.notaireApplication.model.dto.RendezVousDTO;
import com.momo.notaireApplication.repositories.ConversationRepository;
import com.momo.notaireApplication.repositories.MessagesRepository;
import com.momo.notaireApplication.utils.ListUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Transactional
public class MessagingService {
    private ConversationRepository conversationRepository;
    private MessagesRepository messagesRepository;
    private UserService userService;
    private RendezVousService rendezVousService;
    private FichierDocumentService fichierDocumentService;

    public MessagingService(ConversationRepository conversationRepository,
                            MessagesRepository messagesRepository,
                            UserService userService,
                            RendezVousService rendezVousService,
                            FichierDocumentService fichierDocumentService) {
        this.conversationRepository = conversationRepository;
        this.messagesRepository = messagesRepository;
        this.userService = userService;
        this.rendezVousService = rendezVousService;
        this.fichierDocumentService = fichierDocumentService;
    }

    public Conversation createConversation(ConversationDTO conversationDTO,
                                           MessagesDTO messagesDTO,
                                           RendezVousDTO rendezVousDTO,
                                           FichierDocumentDTO fichierDocumentDTO) {
        Conversation conversation = ConversationMapper.instance.toEntity(conversationDTO);

        List<User> users = findAllUsersFromDTO(conversationDTO);
        RendezVous rendezVous = rendezVousService.getRendezVous(rendezVousDTO.getId());
        FichierDocument fichierDocument = fichierDocumentService.getDocument(fichierDocumentDTO.getId());
        addItemsToConversation(conversation,users,rendezVous,fichierDocument,messagesDTO);
        conversation = saveConversation(conversation);
        addConversationToItems(rendezVousDTO, users, conversation);
        return conversation;
    }

    public Conversation addMessage(Long id, MessagesDTO messagesDTO) {
        Conversation conversation = getConversation(id);
        addMessagesDTOtoConversation(messagesDTO, conversation);
        conversation = saveConversation(conversation);
        return conversation;
    }

    @Transactional
    public Conversation getConversation(Long id) {
        return conversationRepository.findById(id).orElseThrow(ConversationNotFoundException::new);
    }

    public ConversationDTO toDTO(Conversation conversation) {
        return ConversationMapper.instance.toDTO(conversation);
    }

    private void addMessagesDTOtoConversation(MessagesDTO messagesDTO, Conversation conversation) {
        Messages messages = MessageMapper.instance.toEntity(messagesDTO);
        if (Objects.nonNull(messages)) {
            User user = userService.getUser(messages.getUser().getId());
            messages.setUser(user);
            messages = messagesRepository.save(messages);
        }
        conversation.setMessages(ListUtil.ajouterObjectAListe(messages, conversation.getMessages()));
    }

    private Conversation saveConversation(Conversation conversation) {
        return conversationRepository.save(conversation);
    }

    private List<User> findAllUsersFromDTO(ConversationDTO conversationDTO) {
        List<User> users = conversationDTO.getUsers()
                .stream()
                .map(userDTO -> userService.getUser(userDTO.getId()))
                .collect(Collectors.toList());
        return users;
    }

    private void addConversationToItems(RendezVousDTO rendezVousDTO, List<User> users, Conversation conversation) {
        addConversationToRendezVous(rendezVousDTO, conversation);
        addConvoToAllUsers(users, conversation);
    }

    private void addConvoToAllUsers(List<User> users, Conversation conversation) {
        users.stream().forEach(user -> {
            user.setConversations(ListUtil.ajouterObjectAListe(conversation, user.getConversations()));
        });
        userService.saveMutlipleUsers(users);
    }
    private void addItemsToConversation(Conversation conversation,List<User> users, RendezVous rendezVous, FichierDocument fichierDocument,MessagesDTO messagesDTO){
        conversation.setRendezVous(rendezVous);
        conversation.setUsers(users);
        conversation.setFichierDocument(fichierDocument);
        addMessagesDTOtoConversation(messagesDTO, conversation);
    }


    private void addConversationToRendezVous(RendezVousDTO rendezVousDTO,Conversation conversation) {
        RendezVous rendezVous = rendezVousService.getRendezVous(rendezVousDTO.getId());
        rendezVous.setConversation(conversation);
        conversation.setRendezVous(rendezVous);
        rendezVousService.saveRendezVous(rendezVous);
    }
}
