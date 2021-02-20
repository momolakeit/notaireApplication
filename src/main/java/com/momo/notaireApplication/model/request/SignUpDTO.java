package com.momo.notaireApplication.model.request;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SignUpDTO {

    private String emailAdress;

    private String prenom;

    private String nom;

    private String password;

    private String role;
}
