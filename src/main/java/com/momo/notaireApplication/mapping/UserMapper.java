package com.momo.notaireApplication.mapping;

import com.momo.notaireApplication.model.db.Client;
import com.momo.notaireApplication.model.db.User;
import com.momo.notaireApplication.model.dto.ClientDTO;
import com.momo.notaireApplication.model.dto.UserDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;


@Mapper
public interface UserMapper {
    UserMapper instance = Mappers.getMapper(UserMapper.class);

    UserDTO toDTO(User user);

    User toEntity(UserDTO userDTO);

}
