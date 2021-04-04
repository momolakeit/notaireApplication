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
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/conversation")
public class MessagingController extends BaseController {


    private MessagingService messagingService;
    private SimpMessagingTemplate simpMessagingTemplate;

    public MessagingController(MessagingService messagingService, SimpMessagingTemplate simpMessagingTemplate) {
        this.messagingService = messagingService;
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    @PostMapping
    @ResponseBody
    public ConversationDTO createConversation(@RequestBody CreateConversationDTO createConversationDTO) {
        Conversation conversation = messagingService.createConversation(createConversationDTO.getConversationDTO(), createConversationDTO.getMessagesDTO());
        return messagingService.toDTO(conversation);
    }

    @MessageMapping("/addMessage/{conversationID}")
    public void addMessage(MessagesDTO messagesDTO, @DestinationVariable Long conversationID) {
        Conversation conversation = messagingService.addMessage(conversationID,messagesDTO);
        simpMessagingTemplate.convertAndSend("/conversation/"+conversation.getId(),messagingService.toDTO(conversation));
    }
    @GetMapping("/getConversation/{conversationID}")
    @ResponseBody
    public ConversationDTO getConversation(@PathVariable Long conversationID){
        Conversation conversation = messagingService.getConversation(conversationID);
        return messagingService.toDTO(conversation);
    }
}
