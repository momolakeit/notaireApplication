package com.momo.notaireApplication.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import java.io.IOException;

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
        Conversation conversation = messagingService.createConversation(createConversationDTO.getConversationDTO(), createConversationDTO.getMessagesDTO(),createConversationDTO.getRendezVousDTO());
        return messagingService.toDTO(conversation);
    }

    @MessageMapping("/addMessage/{conversationID}")
    public void addMessage(MessagesDTO messagesDTO, @DestinationVariable Long conversationID) {
        Conversation conversation = messagingService.addMessage(conversationID,messagesDTO);
        simpMessagingTemplate.convertAndSend("/conversation/"+conversation.getId(),messagingService.toDTO(conversation));
    }
    @MessageMapping("/call/{userId}")
    public void call(String obj, @DestinationVariable Long userId) throws IOException {
        simpMessagingTemplate.convertAndSend("/answerCall/"+userId, new ObjectMapper().writeValueAsString(obj));
    }
    @MessageMapping("/respond/{userId}")
    public void respond(String obj, @DestinationVariable Long userId) throws JsonProcessingException {
        simpMessagingTemplate.convertAndSend("/establishConnection/"+userId,new ObjectMapper().writeValueAsString(obj));
    }
    @MessageMapping("/sendIceCandidate/{userId}")
    public void establishIceConnection(String obj, @DestinationVariable Long userId) throws JsonProcessingException {
        simpMessagingTemplate.convertAndSend("/receiveIceCandidate/"+userId,new ObjectMapper().writeValueAsString(obj));
    }
    @GetMapping("/getConversation/{conversationID}")
    @ResponseBody
    public ConversationDTO getConversation(@PathVariable Long conversationID){
        Conversation conversation = messagingService.getConversation(conversationID);
        return messagingService.toDTO(conversation);
    }
}
