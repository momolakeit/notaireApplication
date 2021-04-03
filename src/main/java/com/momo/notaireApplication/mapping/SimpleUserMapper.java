package com.momo.notaireApplication.mapping;

import com.momo.notaireApplication.model.db.User;
import com.momo.notaireApplication.model.dto.UserDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;


@Mapper
public interface SimpleUserMapper {
    SimpleUserMapper instance = Mappers.getMapper(SimpleUserMapper.class);

    @Mapping(source = "fichierDocuments", target = "fichierDocuments", ignore = true)
    @Mapping(source = "factures", target = "factures", ignore = true)
    @Mapping(source = "rendezVous", target = "rendezVous", ignore = true)
    UserDTO toDTO(User user);

    @Mapping(source = "fichierDocuments", target = "fichierDocuments", ignore = true)
    @Mapping(source = "factures", target = "factures", ignore = true)
    @Mapping(source = "rendezVous", target = "rendezVous", ignore = true)
    User toEntity(UserDTO userDTO);

}
