package com.momo.notaireApplication.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class FactureDTO {
    private Long id;

    private BigDecimal prix;

    private LocalDateTime dateDeCreation;

    private NotaireDTO notaire;

    private ClientDTO client;
}
