package com.momo.notaireApplication.mapping;

import com.momo.notaireApplication.model.db.Messages;
import com.momo.notaireApplication.model.dto.MessagesDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;


@Mapper
public interface MessageMapper {
    MessageMapper instance = Mappers.getMapper(MessageMapper.class);

    @Mapping(source = "user", target = "user", ignore = true)
    MessagesDTO toDTO(Messages messages);

    @Mapping(source = "user", target = "user", ignore = true)
    @Mapping(source = "id", target = "id", ignore = true)
    Messages toEntity(MessagesDTO messagesDTO);

}
