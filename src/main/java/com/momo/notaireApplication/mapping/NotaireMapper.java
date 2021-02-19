package com.momo.notaireApplication.mapping;

import com.momo.notaireApplication.model.db.Notaire;
import com.momo.notaireApplication.model.dto.NotaireDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;


@Mapper
public interface NotaireMapper {
    NotaireMapper instance = Mappers.getMapper(NotaireMapper.class);

    NotaireDTO toDTO(Notaire notaire);

    Notaire toEntity(NotaireDTO notaireDTO);

}