package com.momo.notaireApplication.model.db;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Getter
@Setter
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String emailAdress;

    private String prenom;

    private String nom;

    private String password;

    @ManyToMany
    private List<FichierDocument> fichierDocuments;

    @OneToMany
    private List<Facture> factures;
}
