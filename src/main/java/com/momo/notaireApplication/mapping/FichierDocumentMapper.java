package com.momo.notaireApplication.mapping;

import com.momo.notaireApplication.model.db.FichierDocument;
import com.momo.notaireApplication.model.db.Notaire;
import com.momo.notaireApplication.model.dto.FichierDocumentDTO;
import com.momo.notaireApplication.model.dto.NotaireDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface FichierDocumentMapper {
    FichierDocumentMapper instance = Mappers.getMapper(FichierDocumentMapper.class);

    @Mapping(source = "notaire", target = "notaire", ignore = true)
    @Mapping(source = "client", target = "client", ignore = true)
    FichierDocumentDTO toDTO(FichierDocument fichierDocument);

    @Mapping(source = "notaire", target = "notaire", ignore = true)
    @Mapping(source = "client", target = "client", ignore = true)
    FichierDocument toEntity(FichierDocumentDTO fichierDocumentDTO);
}
