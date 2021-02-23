package com.momo.notaireApplication.mapping;

import com.momo.notaireApplication.model.db.Facture;
import com.momo.notaireApplication.model.dto.FactureDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface FactureMapper {
    FactureMapper instance = Mappers.getMapper(FactureMapper.class);

    @Mapping(source = "users", target = "users", ignore = true)
    FactureDTO toDTO(Facture facture);

    @Mapping(source = "users", target = "users", ignore = true)
    Facture toEntity(FactureDTO factureDTO);
}
