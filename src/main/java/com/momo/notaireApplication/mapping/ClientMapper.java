package com.momo.notaireApplication.mapping;

import com.momo.notaireApplication.model.db.Client;
import com.momo.notaireApplication.model.dto.ClientDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;


@Mapper
public interface ClientMapper {
    ClientMapper instance = Mappers.getMapper(ClientMapper.class);

    ClientDTO toDTO(Client client);

    Client toEntity(ClientDTO clientDTO);

}
