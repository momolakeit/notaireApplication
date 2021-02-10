package com.momo.notaireApplication.mapping;

import com.momo.notaireApplication.model.db.Client;
import com.momo.notaireApplication.model.db.Facture;
import com.momo.notaireApplication.model.db.Notaire;
import com.momo.notaireApplication.model.dto.ClientDTO;
import com.momo.notaireApplication.model.dto.FactureDTO;
import com.momo.notaireApplication.model.dto.NotaireDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper
public interface FactureMapper {
    FactureMapper instance = Mappers.getMapper(FactureMapper.class);

    @Mapping(source = "notaire", target = "notaire", qualifiedByName = "mapNotaireDTO")
    @Mapping(source = "client", target = "client", qualifiedByName = "mapClientDTO")
    FactureDTO toDTO(Facture facture);

    @Mapping(source = "notaire", target = "notaire", qualifiedByName = "mapNotaireEntity")
    @Mapping(source = "client", target = "client", qualifiedByName = "mapClientEntity")
    Facture toEntity(FactureDTO factureDTO);

    @Named("mapNotaireDTO")
    private NotaireDTO mapNotaireDTO(Notaire notaire) {
        return NotaireMapper.instance.toDTO(notaire);
    }
    @Named("mapNotaireEntity")
    private Notaire mapNotaireEntity(NotaireDTO notaireDTO) {
        return NotaireMapper.instance.toEntity(notaireDTO);
    }

    @Named("mapClientDTO")
    private ClientDTO clientToClientDTO(Client client) {
        return ClientMapper.instance.toDTO(client);
    }
    @Named("mapClientEntity")
    private Client clientToClientDTO(ClientDTO clientDTO) {
        return ClientMapper.instance.toEntity(clientDTO);
    }
}
