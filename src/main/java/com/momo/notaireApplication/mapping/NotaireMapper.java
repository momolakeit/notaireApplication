package com.momo.notaireApplication.mapping;

import com.momo.notaireApplication.model.db.Facture;
import com.momo.notaireApplication.model.db.FichierDocument;
import com.momo.notaireApplication.model.db.Notaire;
import com.momo.notaireApplication.model.dto.FactureDTO;
import com.momo.notaireApplication.model.dto.FichierDocumentDTO;
import com.momo.notaireApplication.model.dto.NotaireDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.stream.Collectors;

@Mapper
public interface NotaireMapper {
    NotaireMapper instance = Mappers.getMapper(NotaireMapper.class);

    @Mapping(source = "factures", target = "factures", qualifiedByName = "mapFacturesDTOList")
    @Mapping(source = "fichierDocuments", target = "fichierDocuments", qualifiedByName = "mapFichierDocumentDTOList")
    NotaireDTO toDTO(Notaire notaire);

    @Mapping(source = "factures", target = "factures", qualifiedByName = "mapFacturesEntityList")
    @Mapping(source = "fichierDocuments", target = "fichierDocuments", qualifiedByName = "mapFichierDocumentEntityList")
    Notaire toEntity(NotaireDTO notaireDTO);

    @Named("mapFacturesDTOList")
    private List<FactureDTO> mapFacturesDTOList(List<Facture> factures) {
        return factures
                .stream()
                .map(facture -> FactureMapper.instance.toDTO(facture))
                .collect(Collectors.toList());
    }

    @Named("mapFacturesEntityList")
    private List<Facture> mapFacturesEntityList(List<FactureDTO> factures) {
        return factures
                .stream()
                .map(facture -> FactureMapper.instance.toEntity(facture))
                .collect(Collectors.toList());
    }

    @Named("mapFichierDocumentDTOList")
    private List<FichierDocumentDTO> mapFichierDocumentDTOList(List<FichierDocument> fichierDocuments) {
        return fichierDocuments
                .stream()
                .map(fichierDocument -> FichierDocumentMapper.instance.toDTO(fichierDocument))
                .collect(Collectors.toList());
    }

    @Named("mapFichierDocumentEntityList")
    private List<FichierDocument> mapFichierDocumentEntityList(List<FichierDocumentDTO> fichierDocuments) {
        return fichierDocuments
                .stream()
                .map(fichierDocument -> FichierDocumentMapper.instance.toEntity(fichierDocument))
                .collect(Collectors.toList());
    }
}
