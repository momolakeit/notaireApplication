package com.momo.notaireApplication.mapping;

import com.momo.notaireApplication.model.db.FichierDocument;
import com.momo.notaireApplication.model.db.Notaire;
import com.momo.notaireApplication.model.dto.FichierDocumentDTO;
import com.momo.notaireApplication.model.dto.NotaireDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface FichierDocumentMapper {
    FichierDocumentMapper instance = Mappers.getMapper(FichierDocumentMapper.class);

    FichierDocumentDTO toDTO(FichierDocument fichierDocument);

    FichierDocument toEntity(FichierDocumentDTO fichierDocumentDTO);
}
