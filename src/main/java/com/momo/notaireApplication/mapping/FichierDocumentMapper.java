package com.momo.notaireApplication.mapping;

import com.momo.notaireApplication.model.db.FichierDocument;
import com.momo.notaireApplication.model.dto.FichierDocumentDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface FichierDocumentMapper {
    FichierDocumentMapper instance = Mappers.getMapper(FichierDocumentMapper.class);

    @Mapping(target = "users", ignore = true)
    FichierDocumentDTO toDTO(FichierDocument fichierDocument);

    @Mapping(target = "users", ignore = true)
    FichierDocument toEntity(FichierDocumentDTO fichierDocumentDTO);
}
