package com.momo.notaireApplication.mapping;

import com.momo.notaireApplication.model.db.Conversation;
import com.momo.notaireApplication.model.db.RendezVous;
import com.momo.notaireApplication.model.dto.ConversationDTO;
import com.momo.notaireApplication.model.dto.RendezVousDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;


@Mapper(uses = {MessageMapper.class,SimpleUserMapper.class,RendezVousMapper.class})
public interface ConversationMapper {
    ConversationMapper instance = Mappers.getMapper(ConversationMapper.class);

    ConversationDTO toDTO(Conversation conversation);

    @Mapping(source = "users", target = "users", ignore = true)
    @Mapping(source = "id", target = "id", ignore = true)
    Conversation toEntity(ConversationDTO conversation);

}
