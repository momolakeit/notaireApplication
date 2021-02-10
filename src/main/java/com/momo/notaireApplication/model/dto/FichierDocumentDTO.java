package com.momo.notaireApplication.model.dto;

import com.momo.notaireApplication.model.db.Client;
import com.momo.notaireApplication.model.db.Notaire;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.ManyToMany;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class FichierDocumentDTO {
    private Long id;

    private LocalDateTime localDateTime;

    private byte[] data;

    private NotaireDTO notaire;

    private List<ClientDTO> client;
}
