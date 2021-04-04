package com.momo.notaireApplication.mapping;

import com.momo.notaireApplication.model.db.Notaire;
import com.momo.notaireApplication.model.db.RendezVous;
import com.momo.notaireApplication.model.dto.NotaireDTO;
import com.momo.notaireApplication.model.dto.RendezVousDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;


@Mapper(uses = ConversationMapper.class)
public interface RendezVousMapper {
    RendezVousMapper instance = Mappers.getMapper(RendezVousMapper.class);

    @Mapping(source = "users", target = "users", ignore = true)
    RendezVousDTO toDTO(RendezVous rendezVous);

    @Mapping(source = "users", target = "users", ignore = true)
    RendezVous toEntity(RendezVousDTO rendezVousDTO);

}
