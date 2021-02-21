package com.momo.notaireApplication.model.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class LogInDTO {
    private String emailAdress;
    private String password;
}
