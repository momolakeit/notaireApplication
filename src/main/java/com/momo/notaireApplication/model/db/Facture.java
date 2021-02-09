package com.momo.notaireApplication.model.db;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Facture {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private BigDecimal prix;

    private LocalDateTime dateDeCreation;

    @ManyToOne
    private Notaire notaire;

    @ManyToOne
    private Client client;



}
