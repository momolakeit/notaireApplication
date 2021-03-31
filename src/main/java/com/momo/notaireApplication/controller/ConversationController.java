package com.momo.notaireApplication.controller;

import com.momo.notaireApplication.model.db.Conversation;
import com.momo.notaireApplication.model.dto.ConversationDTO;
import com.momo.notaireApplication.model.dto.JWTResponse;
import com.momo.notaireApplication.model.dto.MessagesDTO;
import com.momo.notaireApplication.model.request.CreateConversationDTO;
import com.momo.notaireApplication.model.request.LogInDTO;
import com.momo.notaireApplication.model.request.SignUpDTO;
import com.momo.notaireApplication.service.MessagingService;
import com.momo.notaireApplication.service.authentification.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/conversation")
public class ConversationController extends BaseController {


    private MessagingService messagingService;

    public ConversationController(MessagingService messagingService) {
        this.messagingService = messagingService;
    }

    @PostMapping
    public ConversationDTO createConversation(@RequestBody CreateConversationDTO createConversationDTO) {
        Conversation conversation = messagingService.createConversation(createConversationDTO.getConversationDTO(), createConversationDTO.getMessagesDTO());
        return messagingService.toDTO(conversation);
    }

    @PostMapping("/addMessage/{conversationID}")
    public ConversationDTO addMessage(@RequestBody MessagesDTO messagesDTO, @PathVariable Long conversationID) {
        Conversation conversation = messagingService.addMessage(conversationID,messagesDTO);
        return messagingService.toDTO(conversation);
    }
}
