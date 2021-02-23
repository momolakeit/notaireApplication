package com.momo.notaireApplication.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class FichierDocumentDTO {
    private Long id;

    private LocalDateTime localDateTime;

    private byte[] data;

    private List<UserDTO> users;
}
