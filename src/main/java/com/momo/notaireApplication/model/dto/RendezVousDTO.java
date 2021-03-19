package com.momo.notaireApplication.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class RendezVousDTO {
    private Long id;

    private LocalDateTime dateDebut;

    private LocalDateTime dateFin;

    private int dureeEnMinute;

    private List<UserDTO> users;
}
