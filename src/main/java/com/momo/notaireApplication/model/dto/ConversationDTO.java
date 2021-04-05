package com.momo.notaireApplication.model.dto;

import com.momo.notaireApplication.model.db.Messages;
import com.momo.notaireApplication.model.db.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ConversationDTO {
    private Long id;

    private List<UserDTO> users;

    private List<MessagesDTO> messages;

    private RendezVousDTO rendezVous;
}
