package com.momo.notaireApplication.service;

import com.momo.notaireApplication.mapping.ConversationMapper;
import com.momo.notaireApplication.mapping.MessageMapper;
import com.momo.notaireApplication.model.db.Conversation;
import com.momo.notaireApplication.model.db.Messages;
import com.momo.notaireApplication.model.db.User;
import com.momo.notaireApplication.model.dto.ConversationDTO;
import com.momo.notaireApplication.model.dto.MessagesDTO;
import com.momo.notaireApplication.repositories.ConversationRepository;
import com.momo.notaireApplication.utils.ListUtil;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MessagingService {
    private ConversationRepository conversationRepository;
    private UserService userService;

    public MessagingService(ConversationRepository conversationRepository, UserService userService) {
        this.conversationRepository = conversationRepository;
        this.userService = userService;
    }

    public ConversationDTO createConversation(ConversationDTO conversationDTO, MessagesDTO messagesDTO) {
        List<User> users = findAllUsersFromDTO(conversationDTO);
        Conversation conversation = ConversationMapper.instance.toEntity(conversationDTO);
        Messages messages = MessageMapper.instance.toEntity(messagesDTO);
        conversation.setMessages(ListUtil.ajouterObjectAListe(messages, conversation.getMessages()));
        conversation.setUsers(users);
        conversation = saveConversation(conversation);
        addConvoToAllUsers(users,conversation);
        return ConversationMapper.instance.toDTO(conversation);
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
    private void addConvoToAllUsers(List<User> users,Conversation conversation){
        users.stream().forEach(user -> {
            user.setConversations(ListUtil.ajouterObjectAListe(conversation,user.getConversations()));
        });
        userService.saveMutlipleUsers(users);
    }
}
