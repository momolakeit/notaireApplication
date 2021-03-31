package com.momo.notaireApplication.model.request;

import com.momo.notaireApplication.model.dto.ConversationDTO;
import com.momo.notaireApplication.model.dto.MessagesDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateConversationDTO {
    private ConversationDTO conversationDTO;
    private MessagesDTO messagesDTO;
}
