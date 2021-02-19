package com.momo.notaireApplication.model.request;

import com.momo.notaireApplication.model.dto.ClientDTO;
import com.momo.notaireApplication.model.dto.NotaireDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class CreateFichierDocumentRequestDTO {
    private NotaireDTO notaireDTO;

    private ClientDTO clientDTO;

}