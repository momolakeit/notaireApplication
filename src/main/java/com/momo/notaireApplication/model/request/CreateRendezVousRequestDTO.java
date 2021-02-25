package com.momo.notaireApplication.model.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CreateRendezVousRequestDTO {
    private Long clientId;

    private Long notaireId;

    private Long date;

    private int dureeEnMinute;
}
