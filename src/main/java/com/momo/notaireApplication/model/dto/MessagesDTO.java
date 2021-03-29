package com.momo.notaireApplication.model.dto;

import com.momo.notaireApplication.model.db.Conversation;
import com.momo.notaireApplication.model.db.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
public class MessagesDTO {
    private Long id;

    private UserDTO user;

    private ConversationDTO conversation;
}
