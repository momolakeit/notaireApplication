package com.momo.notaireApplication.model.dto;

import com.momo.notaireApplication.model.db.Facture;
import com.momo.notaireApplication.model.db.FichierDocument;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.OneToMany;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class UserDTO {
    private Long id;

    private String emailAdress;

    private String prenom;

    private String nom;

    private String password;

    private List<FichierDocumentDTO> fichierDocuments;

    private List<FactureDTO> factures;
}
